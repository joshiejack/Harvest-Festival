package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.task.*;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.tile.TileFestivalPot;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.quests.base.QuestFestivalTimed;
import joshie.harvest.quests.town.festivals.harvest.HarvestSelection;
import joshie.harvest.quests.town.festivals.harvest.ScriptHarvestDecide;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFQuest("festival.harvest")
public class QuestHarvestFestival extends QuestFestivalTimed {
    //TODO Add the location of the pot
    public static final BlockPos POT = new BlockPos(1, 2, 13);
    //TODO: Add beside the pot
    private static final BlockPos BESIDE_POT =  new BlockPos(1, 2, 13);
    private static final BlockPos[] NEAR_POT = new BlockPos[] { new BlockPos(1, 2, 14), new BlockPos(1, 2, 12), new BlockPos(2, 2, 13), new BlockPos(0, 2, 13)};
    private static final Script TASTE = new Script(new ResourceLocation(MODID, "taste"));
    private static final Script DECIDE = new ScriptHarvestDecide();
    private final Map<UUID, HarvestSelection> data = new HashMap<>();

    public QuestHarvestFestival() {
        setNPCs(HFNPCs.FLOWER_GIRL);
    }

    private HarvestSelection getDataForPlayer(EntityPlayer player) {
        UUID uuid = EntityHelper.getPlayerUUID(player);
        if (data.containsKey(uuid)) return data.get(uuid);
        else {
            HarvestSelection selection = new HarvestSelection();
            data.put(uuid, selection);
            return selection;
        }
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        return !getDataForPlayer(player).hasStarted() ? getDataForPlayer(player) : null;
    }

    @Override
    protected boolean isCorrectTime(long time) {
        return time >= 8000L && time <= 20000L;
    }

    @Override
    @Nullable
    protected String getLocalizedScript(EntityPlayer player, NPC npc) {
        return getDataForPlayer(player).getLocalizedScript();
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPC npc) {}

    //Execute the festival
    public void execute(EntityPlayer player, NPCEntity entity) {
        data.values().forEach(HarvestSelection::setStarted);
        syncData(player); //Sync it all up!
        entity.setPath(TaskMove.of(TownHelper.getClosestTownToEntity(player, false).getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, BESIDE_POT)), TaskSpeech.of(TASTE), TaskWait.of(1), TaskSpeech.of(DECIDE), new TaskConsume());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList list = nbt.getTagList("Data", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            HarvestSelection starry = HarvestSelection.fromNBT(tag.getCompoundTag("Data"));
            data.put(uuid, starry);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList list = new NBTTagList();
        for (Entry<UUID, HarvestSelection> entry: data.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UUID", entry.getKey().toString());
            tag.setTag("Data", entry.getValue().toNBT());
            list.appendTag(tag);
        }

        nbt.setTag("Data", list);
        return nbt;
    }

    public static int getPotScore(NPCEntity npc) {
        BlockPos pos = TownHelper.getClosestTownToEntity(npc.getAsEntity(), false).getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, POT);
        if (pos != null) {
            TileEntity tile = npc.getAsEntity().worldObj.getTileEntity(pos);
            if (tile instanceof TileFestivalPot) {
                return ((TileFestivalPot)tile).getScore();
            }
        }

        return 5;
    }

    @HFTask("consume")
    public static class TaskConsume extends TaskElement {
        @Override
        public void execute(NPCEntity npc) {
            satisfied = true;
            TownDataServer data = TownHelper.getClosestTownToEntity(npc.getAsEntity(), false);
            Set<NPC> moving = new HashSet<>();
            moving.add(npc.getNPC());
            List<EntityNPC> list = EntityHelper.getEntities(EntityNPC.class, npc.getAsEntity(), 32D);
            Collections.shuffle(list);
            for (BlockPos target: NEAR_POT) {
                BlockPos pos = data.getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, target);
                if (pos != null) {
                    for (EntityNPC entity: list) {
                        if (!moving.contains(entity.getNPC())) {
                            entity.setPath(TaskMove.of(pos), TaskWait.of(10));
                            moving.add(entity.getNPC());
                            break;
                        }
                    }
                }
            }

            //Complete this quest as well as
            QuestHarvestFestival quest = data.getQuests().getAQuest(HFFestivals.HARVEST_FESTIVAL.getQuest());
            if (quest != null) {
                data.getQuests().markCompleted(npc.getAsEntity().worldObj, null, quest, false);
                int relationship = (getPotScore(npc) - 5) * 500;
                for (UUID uuid: quest.data.keySet()) {
                    for (NPC inhabitant : data.getInhabitants()) {
                        HFTrackers.getPlayerTracker(npc.getAsEntity().worldObj, uuid).getRelationships().affectRelationship(inhabitant, relationship);
                    }
                }
            }
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            satisfied = tag.getBoolean("Satisfied");
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            tag.setBoolean("Satisfied", satisfied);
            return tag;
        }
    }
}
