package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.task.TaskMove;
import joshie.harvest.api.npc.task.TaskSpeech;
import joshie.harvest.api.npc.task.TaskWait;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.tile.TilePlate;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFestival;
import joshie.harvest.quests.selection.FestivalSelection;
import joshie.harvest.quests.town.festivals.cooking.CookingContestEntries;
import joshie.harvest.quests.town.festivals.cooking.CookingContestScript;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

@HFQuest("festival.cooking")
public class QuestContestCooking extends QuestFestival {
    private static final Script SCRIPT = new CookingContestScript();
    private final Selection selection = new FestivalSelection("cooking");
    private static final int QUESTION = 0;
    private static final int FINISH = 1;
    private Random rand = new Random();
    private Utensil category;


    public QuestContestCooking() {
        setNPCs(HFNPCs.GS_OWNER);
    }

    private long time;

    @Override
    public void onQuestSelectedForDisplay(EntityPlayer player, NPCEntity entity) {
        rand.setSeed(HFApi.calendar.getDate(player.worldObj).hashCode());
        category = Utensil.UTENSILS[rand.nextInt(Utensil.UTENSILS.length)];
        time = CalendarHelper.getTime(player.worldObj);
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPCEntity entity) {
        if (!isCorrectTime()) return null;
        if (quest_stage <= QUESTION) {
            return selection;
        } else return null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        if (!isCorrectTime()) return null;
        return "We are describing the contest to the player";
    }

    private boolean isCorrectTime() {
        return time >= 6000 && time <= 18000;
    }

    public CookingContestEntries getContestEntries(NPCEntity entity) {
        return new CookingContestEntries(entity.getAsEntity().getEntityWorld(), entity.getAsEntity(), category);
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        if (!isCorrectTime()) return;
        if (!player.worldObj.isRemote) {
            TownData town = TownHelper.getClosestTownToEntity(player, false);
            BlockPos original = town.getCoordinatesFor(BuildingLocations.PARK_LAMP_BACK);
            //Schedule for Jenni
            EntityAIPathing pathing = ((EntityNPCHuman)entity).getPathing();
            TaskMove point1 = TaskMove.of(town.getCoordinatesFor(BuildingLocations.PARK_STAGE_RIGHT));
            TaskMove point2 = TaskMove.of(town.getCoordinatesFor(BuildingLocations.PARK_STAGE_LEFT));
            TaskSpeech speech = TaskSpeech.of(SCRIPT);
            pathing.setPath(point1, point2, point1, speech);
            CookingContestEntries entries = getContestEntries(entity);
            Set<UUID> used = new HashSet<>();
            List<EntityNPCHuman> npcs = EntityHelper.getEntities(EntityNPCHuman.class, player.worldObj, original, 32D, 5D);
            for (EntityNPCHuman theNPC: npcs) {
                if (entries.isEntered(theNPC.getNPC())) {
                    for (int i = 0; i < 4; i++) {
                        BlockPos pos = entries.getStandLocations(town)[i];
                        TileEntity tile = player.worldObj.getTileEntity(pos);
                        if (tile instanceof TilePlate) {
                            TilePlate stand = ((TilePlate)tile);
                            if (stand.getContents() == null || used.contains(stand.getUUID())) {
                                stand.setContents(entries.getEntryForNPC(theNPC.getNPC()));
                                theNPC.getPathing().setPath(TaskMove.of(entries.getWalkLocations(town)[i]), TaskWait.of(10));
                                break;
                            } else if (stand.getContents() != null && stand.getUUID() != null) used.add(stand.getUUID());
                        }
                    }
                }
            }

            increaseStage(player);
        } else if (quest_stage == FINISH) {
            complete(player); //Finish the contest
            CookingContestEntries entries = getContestEntries(entity);
            if (entries.isWinner(player)) {
                player.addStat(HFAchievements.corn);
                HFApi.quests.completeQuestConditionally(Quests.SELL_CORN, player);
                rewardGold(player, entries.getPrize());
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private Map<BlockPos, BlockPos> buildEmptyMap(World world, TownData town) {
        Map<BlockPos, BlockPos> emptyList = new HashMap<>();
        addToEmptyListIfStandIsEmpty(emptyList, world, town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND1).down(), town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND1_HUMAN));
        addToEmptyListIfStandIsEmpty(emptyList, world, town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND2).down(), town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND2_HUMAN));
        addToEmptyListIfStandIsEmpty(emptyList, world, town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND3).down(), town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND3_HUMAN));
        addToEmptyListIfStandIsEmpty(emptyList, world, town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND4).down(), town.getCoordinatesFor(BuildingLocations.PARK_STAGE_STAND4_HUMAN));
        return emptyList;
    }

    private void addToEmptyListIfStandIsEmpty(Map<BlockPos, BlockPos> emptyList, World world, BlockPos pos, BlockPos pos2) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TilePlate) {
            ItemStack stack = ((TilePlate)tile).getContents();
            if (stack == null) emptyList.put(pos, pos2);
        }
    }
}
