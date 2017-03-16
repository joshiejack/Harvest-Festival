package joshie.harvest.quests.town.festivals;

import joshie.harvest.animals.entity.EntityHarvestChicken;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.town.BuildingLocations.PARK_SHEEP_JUDGE;

@HFQuest("festival.chicken")
public class QuestContestChicken extends QuestAnimalContest<EntityHarvestChicken> {
    private static final String PREFIX = "chicken";
    //TODO: Add the stall positions for walking based on serious setup
    private static final BlockPos[] STALLS = new BlockPos[] { new BlockPos(6, 1, 12), new BlockPos(10, 1, 21), new BlockPos(27, 1, 20), new BlockPos(27, 1, 5) };
    private static final NPC[] NPCS = new NPC[] { HFNPCs.CAFE_GRANNY, HFNPCs.FLOWER_GIRL, HFNPCs.DAUGHTER_CHILD, HFNPCs.CLOCKMAKER_CHILD, HFNPCs.TRADER, HFNPCs.CARPENTER, HFNPCs.GS_OWNER };
    private static final String[] NAMES = new String[] { "Spock", "Taco", "Torchic", "Sugar", "Nugget", "Robin", "Robyn", "Brenda", "Cora", "Maggie", "Peanut", "Pebbles", "Chica", "Cheyenne"};
    private static final Script FINISH = new HFScript(PREFIX + "_finish");
    private static final Script JUDGE_1 = new ContestJudgingScript(PREFIX, 1).setNPC(HFNPCs.POULTRY);
    private static final Script JUDGE_2 = new ContestJudgingScript(PREFIX, 2).setNPC(HFNPCs.POULTRY);
    private static final Script JUDGE_3 = new ContestJudgingScript(PREFIX, 3).setNPC(HFNPCs.POULTRY);
    private static final Script JUDGE_4 = new ContestJudgingScript(PREFIX, 4).setNPC(HFNPCs.POULTRY);
    private static final Script WINNER = new ContestWinningScript(PREFIX).setNPC(HFNPCs.POULTRY);
    //TODO: Add the stand positions for walking based on serious setup
    private static final BlockPos STAND1 = new BlockPos(8, 1, 12);
    //TODO: Add the stand positions for walking based on serious setup
    private static final BlockPos STAND2 = new BlockPos(10, 1, 18);
    //TODO: Add the stand positions for walking based on serious setup
    private static final BlockPos STAND3 = new BlockPos(27, 1, 16);
    //TODO: Add the stand positions for walking based on serious setup
    private static final BlockPos STAND4 = new BlockPos(23, 1, 6);

    public QuestContestChicken() {
        super(HFNPCs.POULTRY, PREFIX);
    }

    @Override
    protected AnimalContestEntries createEntries() {
        return new AnimalContestEntries<>(EntityHarvestChicken.class, STALLS, NPCS, NAMES);
    }

    @Override
    public ItemStack getReward(Place place) {
        switch (place) {
            case FIRST:
                return HFCooking.MEAL.getCreativeStack(Meal.OMELET);
            case SECOND:
                return HFCooking.MEAL.getCreativeStack(Meal.EGG_SCRAMBLED);
            default:
                return HFCooking.MEAL.getCreativeStack(Meal.EGG_BOILED);
        }
    }

    @Override //TODO: Change park_sheep_judge for walking based on serious setup of where the chicken judge stand is
    public void execute(Town town, EntityPlayer player, NPCEntity npc) {
        npc.setPath(move(STAND1), speech(JUDGE_1), move(STAND2), speech(JUDGE_2), move(STAND3), speech(JUDGE_3), move(STAND4), speech(JUDGE_4),
                wait(1), speech(FINISH), move(PARK_SHEEP_JUDGE), speech(WINNER), new ContestTaskWinner(HFFestivals.CHICKEN_FESTIVAL));
    }
}
