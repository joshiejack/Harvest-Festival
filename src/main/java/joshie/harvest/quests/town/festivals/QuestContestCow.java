package joshie.harvest.quests.town.festivals;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.task.TaskSpeech;
import joshie.harvest.api.npc.task.TaskWait;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.base.other.HFScript;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.quests.town.festivals.contest.ContestEntries;
import joshie.harvest.quests.town.festivals.contest.ContestJudgingScript;
import joshie.harvest.quests.town.festivals.contest.ContestWinningScript;
import joshie.harvest.quests.town.festivals.contest.TaskWinner;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.town.BuildingLocations.*;

@HFQuest("festival.cow")
public class QuestContestCow extends QuestAnimalContest<EntityHarvestCow> {
    private static final BuildingLocation[] LOCATIONS = new BuildingLocation[] { BuildingLocations.PARK_COW_STALL_1, BuildingLocations.PARK_COW_STALL_2, BuildingLocations.PARK_CENTRE, BuildingLocations.PARK_COW_STALL_4 };
    private static final NPC[] NPCS = new NPC[] { HFNPCs.BARN_OWNER, HFNPCs.GS_OWNER, HFNPCs.TRADER, HFNPCs.CARPENTER };
    private static final String[] NAMES = new String[] { "Anabelle", "Maybelle", "Daisy", "Miltank", "Bessie", "Clarabelle", "Dorothy", "Ella", "Molly", "Bella"};
    private static final Script COW_FINISH = new HFScript("cow_finish");
    private static final Script COW_JUDGE_1 = new ContestJudgingScript("cow", 1).setNPC(HFNPCs.MILKMAID);
    private static final Script COW_JUDGE_2 = new ContestJudgingScript("cow", 2).setNPC(HFNPCs.MILKMAID);
    private static final Script COW_JUDGE_3 = new ContestJudgingScript("cow", 3).setNPC(HFNPCs.MILKMAID);
    private static final Script COW_JUDGE_4 = new ContestJudgingScript("cow", 4).setNPC(HFNPCs.MILKMAID);
    private static final Script COW_WINNER = new ContestWinningScript("cow").setNPC(HFNPCs.MILKMAID);

    public QuestContestCow() {
        super(new ContestEntries<>(EntityHarvestCow.class, LOCATIONS, NPCS, NAMES), "cow");
        setNPCs(HFNPCs.MILKMAID);
    }

    @Override
    public ItemStack getReward(Place place) {
        switch (place) {
            case FIRST:
                return HFCooking.MEAL.getCreativeStack(Meal.MILK_STRAWBERRY);
            case SECOND:
                return HFCooking.MEAL.getCreativeStack(Meal.MILK_HOT);
            default:
                return new ItemStack(Items.WHEAT);
        }
    }

    @Override
    public void execute(EntityPlayer player, NPCEntity npc) {
        TownData town = TownHelper.getClosestTownToEntity(player, false);
        entries.startContest(player); //Spawn any relevant data
        setStage(QuestContestCow.START); //Mark as having started
        syncData(player); //Sync up to the client
        npc.setPath(getMove(town, PARK_COW_1), TaskSpeech.of(QuestContestCow.COW_JUDGE_1), getMove(town, PARK_COW_2), TaskSpeech.of(QuestContestCow.COW_JUDGE_2),
                getMove(town, PARK_COW_3), TaskSpeech.of(QuestContestCow.COW_JUDGE_3), getMove(town, PARK_COW_4), TaskSpeech.of(QuestContestCow.COW_JUDGE_4), TaskWait.of(1),
                TaskSpeech.of(QuestContestCow.COW_FINISH), getMove(town, PARK_COW_JUDGE), TaskSpeech.of(QuestContestCow.COW_WINNER), new TaskWinner(HFFestivals.COW_FESTIVAL));
    }
}
