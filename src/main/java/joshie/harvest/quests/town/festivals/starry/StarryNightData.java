package joshie.harvest.quests.town.festivals.starry;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.api.npc.task.TaskSpeech;
import joshie.harvest.api.npc.task.TaskWait;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockStand.Stand;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.tile.TilePlate;
import joshie.harvest.quests.town.festivals.QuestStarryNight;
import joshie.harvest.quests.town.festivals.contest.ContestEntries;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.cooking.item.ItemMeal.Meal.*;
import static joshie.harvest.quests.town.festivals.QuestStarryNight.GOODBYE;
import static joshie.harvest.quests.town.festivals.QuestStarryNight.WELCOME;

public class StarryNightData extends Selection<QuestStarryNight> {
    //TODO: Add locations for the plates
    private static final BlockPos[] plates = new BlockPos[] { new BlockPos(10, 2, 13), new BlockPos(10, 2, 14), new BlockPos(10, 2, 15), new BlockPos(10, 2, 16), new BlockPos(10, 2, 17), new BlockPos(10, 2, 18) };
    private static final Meal[] meals = new Meal[] { FISH_GRILLED, POTSTICKER, RISOTTO, SASHIMI_CHIRASHI, DUMPLINGS, STEW_PUMPKIN, STIR_FRY };
    private static final String prefix = "harvestfestival.quest.festival.starry.night";
    private static final String[] lines2 = new String[] { "harvestfestival.quest.festival.starry.night.ready", "harvestfestival.quest.festival.starry.night.go", "harvestfestival.quest.festival.starry.night.no"};
    private boolean completed;
    private boolean chat;
    private NPC invited;

    public StarryNightData() {
        super("harvestfestival.quest.festival.starry.night.what", "harvestfestival.quest.festival.starry.night.invite", "harvestfestival.quest.festival.starry.night.chat");
    }

    @Override
    public String[] getText(@Nonnull EntityPlayer player, QuestStarryNight quest) {
        return invited == null ? lines : lines2;
    }

    public Selection getSelection(long time) {
        return !chat && (invited == null || time >= 18000L || time < 6000L) ? this : null;
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public String getLocalized(String quest, Object... format) {
        if (format.length == 0) return I18n.translateToLocal(prefix + "." + quest.replace("_", ""));
        else return I18n.translateToLocalFormatted(prefix + "." + quest.replace("_", ""), format);
    }

    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(NPC npc) {
        if (chat) return null;
        else if (invited == null) return TextHelper.getSpeech(npc, "festival.starry.night.invite");
        else if (completed && invited == npc) return TextHelper.getSpeech(npc, "festival.starry.night.start");
        else return invited == npc ? TextHelper.getSpeech(npc, "festival.starry.night.tonight") : null;
    }

    public boolean isChatting() {
        if (chat) {
            chat = false;
            return true;
        } else return false;
    }

    public boolean isFinished() {
        return completed;
    }

    public boolean isInvited(NPCEntity npc) {
        return invited != null && (invited == npc.getNPC() || invited.getFamily().contains(npc.getNPC()));
    }

    private void spawnPlateAtLocation(EntityPlayer player, World world, Set<Meal> used, BlockPos pos) {
        world.setBlockState(pos, HFCore.STAND.getStateFromEnum(Stand.PLATE));
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TilePlate) {
            ((TilePlate)tile).setContents(HFCooking.MEAL.getCreativeStack(ContestEntries.getNextEntry(player, used, meals)));
        }
    }

    private void startSequence(EntityPlayer player, NPCEntity entity) {
        Set<Meal> used = new HashSet<>();
        TownData data = TownHelper.getClosestTownToEntity(player, false);
        Set<BlockPos> positions = new HashSet<>();
        BlockPos playerPlate = data.getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, plates[0]);
        BlockPos npcPlate = data.getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, plates[1]);
        if (playerPlate != null) positions.add(playerPlate);
        if (npcPlate != null) positions.add(npcPlate);
        Set<NPC> family = entity.getNPC().getFamily(); //Family
        for (int i = 2; i < family.size() + 2; i++) {
            BlockPos pos = data.getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, plates[i]);
            if (pos != null) positions.add(pos);
        }

        positions.stream().forEach(pos -> spawnPlateAtLocation(player, player.worldObj, used, pos));
        HFApi.player.getRelationsForPlayer(player).affectRelationship(entity.getNPC(), 5000);
        for (NPC npc: family) {
            HFApi.player.getRelationsForPlayer(player).affectRelationship(npc, 2500);
        }

        //Have all the npcs walk to their seat and sit down
        //Have the main person thank everyone for coming tonight and make a speech
        //Have everyone dig in to their meal
        entity.setPath(TaskSpeech.of(WELCOME), TaskWait.of(1), new ConsumeFood(positions, false), TaskSpeech.of(GOODBYE), new ConsumeFood(positions, true));
    }

    private static class ConsumeFood extends TaskElement {
        private Set<BlockPos> positions;
        private boolean destroy;
        public ConsumeFood(Set<BlockPos> pos, boolean destroy) {
            this.positions = pos;
            this.destroy = destroy;
        }

        @Override
        public void execute(EntityAgeable npc) {
            super.execute(npc);
            for (BlockPos pos: positions) {
                if (destroy) npc.worldObj.setBlockToAir(pos);
                else {
                    TileEntity tile = npc.worldObj.getTileEntity(pos);
                    if (tile instanceof TilePlate) {
                        ((TilePlate) tile).setContents(null);
                    }
                }
            }

            if (destroy) { //Complete this quest too
                QuestStarryNight quest = TownHelper.getClosestTownToEntity(npc, false).getQuests().getAQuest(HFFestivals.STARRY_NIGHT.getQuest());
                if (quest != null) {
                    TownHelper.getClosestTownToEntity(npc, false).getQuests().markCompleted(npc.worldObj, null, quest, false);
                }
            }
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            destroy = tag.getBoolean("Destroy");
            positions = new HashSet<>();
            NBTTagList list = tag.getTagList("Positions", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound nbt = list.getCompoundTagAt(i);
                BlockPos pos = BlockPos.fromLong(nbt.getLong("Position"));
                positions.add(pos);
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            tag.setBoolean("Destroy", destroy);
            NBTTagList list = new NBTTagList();
            for (BlockPos pos: positions) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setLong("Position", pos.toLong());
                list.appendTag(nbt);
            }
            tag.setTag("Positions", list);
            return tag;
        }
    }

    @Override
    public Result onSelected(EntityPlayer player, NPCEntity entity, QuestStarryNight quest, int option) {
        if (invited == null) {
            //Option1 = Invite to Starry Night
            if (option == 1) invited = entity.getNPC();
            else chat = true;
        } else {
            if (option == 1) {
                chat = true;
                completed = true;
                startSequence(player, entity);
            } else return Result.DENY;
        }

        quest.syncData(player); //Resync to client
        //Option2 = Chat
        return Result.ALLOW;
    }

    /////////////////////////////// Saving and Loading /////////////////////////////////
    public static StarryNightData fromNBT(NBTTagCompound tag) {
        StarryNightData data =  new StarryNightData();
        data.chat = tag.getBoolean("Chatting");
        data.completed = tag.getBoolean("Completed");
        if (tag.hasKey("NPC")) data.invited = NPC.REGISTRY.get(new ResourceLocation(tag.getString("NPC")));
        return data;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Completed", completed);
        tag.setBoolean("Chatting", chat);
        if (invited != null) tag.setString("NPC", invited.getResource().toString());
        return tag;
    }
}
