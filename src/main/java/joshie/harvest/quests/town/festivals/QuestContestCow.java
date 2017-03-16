package joshie.harvest.quests.town.festivals;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.town.Town;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.base.other.HFScript;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.quests.town.festivals.contest.ContestJudgingScript;
import joshie.harvest.quests.town.festivals.contest.ContestTaskWinner;
import joshie.harvest.quests.town.festivals.contest.ContestWinningScript;
import joshie.harvest.quests.town.festivals.contest.animal.AnimalContestEntries;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.town.BuildingLocations.PARK_COW_JUDGE;

@HFQuest("festival.cow")
public class QuestContestCow extends QuestAnimalContest<EntityHarvestCow> {
    private static final String PREFIX = "cow";
    private static final BlockPos[] LOCATIONS = new BlockPos[] { new BlockPos(10, 1, 13), new BlockPos(17, 1, 8), new BlockPos(23, 1, 15), new BlockPos(27, 1, 22) };
    private static final NPC[] NPCS = new NPC[] { HFNPCs.BARN_OWNER, HFNPCs.GS_OWNER, HFNPCs.TRADER, HFNPCs.CARPENTER, HFNPCs.CLOCKMAKER_CHILD, HFNPCs.DAUGHTER_CHILD };
    private static final String[] NAMES = new String[] { "Anabelle", "Maybelle", "Daisy", "Miltank", "Bessie", "Clarabelle", "Dorothy", "Ella", "Molly", "Bella", "Bertha"};
    private static final Script FINISH = new HFScript(PREFIX + "_finish");
    private static final Script JUDGE_1 = new ContestJudgingScript(PREFIX, 1).setNPC(HFNPCs.MILKMAID);
    private static final Script JUDGE_2 = new ContestJudgingScript(PREFIX, 2).setNPC(HFNPCs.MILKMAID);
    private static final Script JUDGE_3 = new ContestJudgingScript(PREFIX, 3).setNPC(HFNPCs.MILKMAID);
    private static final Script JUDGE_4 = new ContestJudgingScript(PREFIX, 4).setNPC(HFNPCs.MILKMAID);
    private static final Script WINNER = new ContestWinningScript(PREFIX).setNPC(HFNPCs.MILKMAID);
    private static final BlockPos STAND1 = new BlockPos(12, 1, 13);
    private static final BlockPos STAND2 = new BlockPos(17, 1, 10);
    private static final BlockPos STAND3 = new BlockPos(23, 1, 17);
    private static final BlockPos STAND4 = new BlockPos(27, 1, 20);

    public QuestContestCow() {
        super(HFNPCs.MILKMAID, PREFIX);
    }

    @Override
    protected AnimalContestEntries createEntries() {
        return new AnimalContestEntries<>(EntityHarvestCow.class, LOCATIONS, NPCS, NAMES);
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
        npc.setPath(move(STAND1), speech(JUDGE_1), move(STAND2), speech(JUDGE_2), move(STAND3), speech(JUDGE_3), move(STAND4), speech(JUDGE_4),
                wait(1), speech(FINISH), move(PARK_COW_JUDGE), speech(WINNER), new ContestTaskWinner(HFFestivals.COW_FESTIVAL));
    }
}
