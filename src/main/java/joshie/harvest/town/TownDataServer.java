package joshie.harvest.town;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.gathering.GatheringData;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCHelper;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCHuman;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TownDataServer extends TownData {
    public GatheringData gathering = new GatheringData();
    private Set<ResourceLocation> deadVillagers = new HashSet<>();

    public TownDataServer() {}
    public TownDataServer(BlockPos pos) {
        townCentre = pos;
        uuid = UUID.randomUUID();
    }

    public boolean isDead(INPC npc) {
        return deadVillagers.contains(((NPC)npc).getRegistryName());
    }

    public void markNPCDead(ResourceLocation name) {
        deadVillagers.add(name);
    }

    public EntityNPCBuilder getBuilder(WorldServer world) {
        return (EntityNPCBuilder) world.getEntityFromUuid(getID());
    }

    public boolean setBuilding(World world, BuildingImpl building, BlockPos pos, Mirror mirror, Rotation rotation) {
        BuildingStage stage = new BuildingStage(building, pos, mirror, rotation);
        if (!this.building.contains(stage)) {
            this.building.addLast(stage);
            HFTrackers.markDirty(world);
            return true;
        }

        return false;
    }

    public void finishBuilding(World world) {
        this.building.removeFirst(); //Remove the first building
        HFTrackers.markDirty(world);
    }

    @Override
    public void newDay(World world) {
        gathering.newDay(world, buildings.values());
        for (ResourceLocation villager: deadVillagers) {
            NPC npc = NPCRegistry.REGISTRY.getValue(villager);
            if (npc != HFNPCs.GODDESS) {
                EntityNPCHuman entity = NPCHelper.getEntityForNPC(world, npc);
                entity.setPosition(townCentre.getX(), townCentre.getY(), townCentre.getZ());
                entity.resetSpawnHome();
                BlockPos pos = entity.getHomeCoordinates();
                entity.setPositionAndUpdate(pos.getX(), pos.getY() + 0.5, pos.getZ());
                if (npc == HFNPCs.BUILDER) entity.setUniqueId(getID()); //Keep the Unique ID the same
                world.spawnEntityInWorld(entity);
            }
        }

        deadVillagers = new HashSet<>(); //Reset the dead villagers
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        gathering.readFromNBT(nbt);
        nbt.setTag("DeadVillagers", NBTHelper.writeResourceSet(deadVillagers));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        gathering.writeToNBT(nbt);
        deadVillagers = NBTHelper.readResourceSet(nbt.getTagList("DeadVillagers", 8));
    }
}
