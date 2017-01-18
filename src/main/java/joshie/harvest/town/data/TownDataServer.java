package joshie.harvest.town.data;

import com.google.common.cache.Cache;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestType;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.interfaces.IQuestMaster;
import joshie.harvest.gathering.GatheringData;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.NPCRegistry;
import joshie.harvest.npcs.entity.EntityNPCBuilder;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.data.QuestDataServer;
import joshie.harvest.quests.packet.PacketQuest;
import joshie.harvest.town.packet.PacketDailyQuest;
import joshie.harvest.town.packet.PacketNewBuilding;
import joshie.harvest.town.packet.PacketSyncBuilding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.*;

public class TownDataServer extends TownData<QuestDataServer> implements IQuestMaster {
    public final GatheringData gathering = new GatheringData();
    private Set<ResourceLocation> deadVillagers = new HashSet<>();
    private final QuestDataServer quests = new QuestDataServer(this);
    private int dimension;

    public TownDataServer() {}
    public TownDataServer(int dimension, BlockPos townCentre, CalendarDate date) {
        this.dimension = dimension;
        this.townCentre = townCentre;
        this.uuid = UUID.randomUUID();
        this.birthday = date.copy();
    }

    @Override
    public QuestDataServer getQuests() {
        return quests;
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.TOWN;
    }

    @Override
    public void sync(@Nullable EntityPlayer player, PacketQuest packet) {
        if (player != null) PacketHandler.sendToClient(packet.setUUID(getID()), player);
        else PacketHandler.sendToDimension(dimension, packet.setUUID(getID()));
    }

    private boolean isDead(NPC npc) {
        return deadVillagers.contains(((NPC)npc).getRegistryName());
    }

    public void createNewBuilder(World world, BlockPos pos) {
        if (!isDead(HFNPCs.BUILDER)) {
            EntityNPCBuilder creator = new EntityNPCBuilder(world);
            creator.setPositionAndUpdate(pos.getX(), pos.getY() + 1.5D, pos.getZ());
            creator.setSpawnHome(this); //Set the spawn town
            creator.setUniqueId(getID()); //Marking the builder as having the same data
            world.spawnEntityInWorld(creator); //Towns owner now spawned
        }
    }

    public void markNPCDead(ResourceLocation name) {
        deadVillagers.add(name);
    }

    public EntityNPCBuilder getBuilder(WorldServer world) {
        return (EntityNPCBuilder) world.getEntityFromUuid(getID());
    }

    public void syncBuildings(World world) {
        PacketHandler.sendToDimension(world.provider.getDimension(), new PacketSyncBuilding(getID(), this.building));
    }

    public boolean setBuilding(World world, Building building, BlockPos pos, Rotation rotation) {
        BuildingStage stage = new BuildingStage(building, pos, rotation);
        if (!this.building.contains(stage)) {
            this.building.addLast(stage);
            HFTrackers.markDirty(world);
            syncBuildings(world);
            return true;
        }

        return false;
    }

    public void finishBuilding(World world) {
        this.building.removeFirst(); //Remove the first building
        HFTrackers.markDirty(world);
    }

    public void addBuilding(World world, Building building, Rotation rotation, BlockPos pos) {
        TownBuilding newBuilding = new TownBuilding(building, rotation, pos);
        buildings.put(Building.REGISTRY.getKey(building), newBuilding);
        PacketHandler.sendToDimension(world.provider.getDimension(), new PacketNewBuilding(uuid, newBuilding));
        HFTrackers.markDirty(world);
    }

    private boolean isRepeatable(World world, Quest quest) {
        if (!quest.isRepeatable()) return false;
        if (quest.getDaysBetween() == 0) return true;
        else {
            CalendarDate date = getQuests().getLastCompletionOfQuest(quest);
            return date == null || CalendarHelper.getDays(date, HFApi.calendar.getDate(world)) >= quest.getDaysBetween();
        }
    }

    public void generateNewDailyQuest(World world) {
        List<Quest> quests = new ArrayList<>();
        for (Quest quest: Quest.REGISTRY) {
            if (isRepeatable(world, quest) || !getQuests().getFinished().contains(quest)) {
                if (!getQuests().getCurrent().contains(quest)) {
                    if (quest.canStartDailyQuest(world, townCentre)) {
                        quests.add(quest);
                    }
                }
            }
        }

        if (quests.size() > 0) {
            dailyQuest = quests.get(world.rand.nextInt(quests.size()));
        } else dailyQuest = null;

        PacketHandler.sendToDimension(world.provider.getDimension(), new PacketDailyQuest(uuid, dailyQuest));
    }

    public void newDay(World world, Cache<BlockPos, Boolean> isFar, CalendarDate today, CalendarDate yesterday) {
        if (world.isBlockLoaded(getTownCentre())) {
            shops.newDay(world, uuid);
            gathering.newDay(world, townCentre, buildings.values(), isFar);
            generateNewDailyQuest(world);
            for (ResourceLocation villager: deadVillagers) {
                NPC npc = NPCRegistry.REGISTRY.getValue(villager);
                if (npc != HFNPCs.GODDESS) {
                    EntityNPCHuman entity = NPCHelper.getEntityForNPC(world, npc);
                    entity.setPosition(townCentre.getX(), townCentre.getY(), townCentre.getZ());
                    entity.resetSpawnHome();
                    BlockPos pos = entity.getHomeCoordinates();
                    int attempts = 0;
                    while (!EntityHelper.isSpawnable(world, pos) && attempts < 64) {
                        pos = pos.add(world.rand.nextInt(16) - 8, world.rand.nextInt(8), world.rand.nextInt(16) - 8);
                        attempts++;
                    }

                    entity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
                    if (npc == HFNPCs.BUILDER) entity.setUniqueId(getID()); //Keep the Unique ID the same
                    world.spawnEntityInWorld(entity);
                }
            }

            //Update the buildings
            for (TownBuilding building: buildings.values()) {
                building.building.newDay(world, building.pos, building.rotation, today, yesterday);
            }

            deadVillagers = new HashSet<>(); //Reset the dead villagers
        }
    }

    public Rotation getFacingFor(ResourceLocation resource) {
        TownBuilding building = buildings.get(resource);
        if (building == null) return null;
        return building.getFacing();
    }

    //Called to sync the data about this town to the client
    public void writePacketNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        quests.readFromNBT(nbt);
        gathering.readFromNBT(nbt);
        dimension = nbt.getInteger("Dimension");
        deadVillagers = NBTHelper.readResourceSet(nbt, "DeadVillagers");
        //TODO: Remove in 0.7+
        if (!nbt.hasKey("CurrentQuests") && !nbt.hasKey("FinishedQuests")) {
            if (buildings.containsKey(HFBuildings.CAFE.getRegistryName())) quests.getFinished().add(Quests.BUILDING_CAFE);
            if (buildings.containsKey(HFBuildings.FISHING_HUT.getRegistryName())) quests.getFinished().add(Quests.BUILDING_FISHER);
            if (buildings.containsKey(HFBuildings.BLACKSMITH.getRegistryName())) quests.getFinished().add(Quests.BUILDING_BLACKSMITH);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        quests.writeToNBT(nbt);
        gathering.writeToNBT(nbt);
        nbt.setInteger("Dimension", dimension);
        nbt.setTag("DeadVillagers", NBTHelper.writeResourceSet(deadVillagers));
    }
}
