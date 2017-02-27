package joshie.harvest.quests.town.festivals;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.task.TaskSpeech;
import joshie.harvest.api.npc.task.TaskWait;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.town.Town;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.town.BuildingLocations.*;

@HFQuest("festival.cow")
public class QuestContestCow extends QuestAnimalContest<EntityHarvestCow> {
    private static final String ANIMAL = "cow";
    private static final BuildingLocation[] LOCATIONS = new BuildingLocation[] { PARK_COW_STALL_1, PARK_COW_STALL_2, PARK_CENTRE, PARK_COW_STALL_4 };
    private static final NPC[] NPCS = new NPC[] { HFNPCs.BARN_OWNER, HFNPCs.GS_OWNER, HFNPCs.TRADER, HFNPCs.CARPENTER, HFNPCs.CLOCKMAKER_CHILD, HFNPCs.DAUGHTER_CHILD };
    private static final String[] NAMES = new String[] { "Anabelle", "Maybelle", "Daisy", "Miltank", "Bessie", "Clarabelle", "Dorothy", "Ella", "Molly", "Bella", "Bertha"};
    private static final Script FINISH = new HFScript(ANIMAL + "_finish");
    private static final Script JUDGE_1 = new ContestJudgingScript(ANIMAL, 1).setNPC(HFNPCs.MILKMAID);
    private static final Script JUDGE_2 = new ContestJudgingScript(ANIMAL, 2).setNPC(HFNPCs.MILKMAID);
    private static final Script JUDGE_3 = new ContestJudgingScript(ANIMAL, 3).setNPC(HFNPCs.MILKMAID);
    private static final Script JUDGE_4 = new ContestJudgingScript(ANIMAL, 4).setNPC(HFNPCs.MILKMAID);
    private static final Script WINNER = new ContestWinningScript(ANIMAL).setNPC(HFNPCs.MILKMAID);

    public QuestContestCow() {
        super(HFNPCs.MILKMAID, new ContestEntries<>(EntityHarvestCow.class, LOCATIONS, NPCS, NAMES), ANIMAL);
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
    public void execute(Town town, EntityPlayer player, NPCEntity npc) {
        npc.setPath(getMove(town, PARK_COW_1), TaskSpeech.of(QuestContestCow.JUDGE_1), getMove(town, PARK_COW_2), TaskSpeech.of(QuestContestCow.JUDGE_2),
                getMove(town, PARK_COW_3), TaskSpeech.of(QuestContestCow.JUDGE_3), getMove(town, PARK_COW_4), TaskSpeech.of(QuestContestCow.JUDGE_4), TaskWait.of(1),
                TaskSpeech.of(QuestContestCow.FINISH), getMove(town, PARK_COW_JUDGE), TaskSpeech.of(QuestContestCow.WINNER), new TaskWinner(HFFestivals.COW_FESTIVAL));
    }
}
