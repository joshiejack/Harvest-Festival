package joshie.harvest.quests.town.festivals;

import joshie.harvest.animals.entity.EntityHarvestSheep;
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
import joshie.harvest.shops.HFShops;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;
import static joshie.harvest.town.BuildingLocations.PARK_SHEEP_JUDGE;

@HFQuest("festival.sheep")
public class QuestContestSheep extends QuestAnimalContest<EntityHarvestSheep> {
    private static final String PREFIX = "sheep";
    private static final BlockPos[] STALLS = new BlockPos[] { new BlockPos(6, 1, 12), new BlockPos(10, 1, 21), new BlockPos(27, 1, 20), new BlockPos(27, 1, 5) };
    private static final NPC[] NPCS = new NPC[] { HFNPCs.CAFE_GRANNY, HFNPCs.FLOWER_GIRL, HFNPCs.MILKMAID, HFNPCs.DAUGHTER_ADULT, HFNPCs.TRADER, HFNPCs.GS_OWNER };
    private static final String[] NAMES = new String[] { "Fluffy", "Flaafy", "Maisy", "Mareep", "Shaggy", "Fae", "Emma", "Dolly", "Sally", "Larry", "Shaun"};
    private static final Script FINISH = new HFScript(PREFIX + "_finish");
    private static final Script JUDGE_1 = new ContestJudgingScript(PREFIX, 1).setNPC(HFNPCs.BARN_OWNER);
    private static final Script JUDGE_2 = new ContestJudgingScript(PREFIX, 2).setNPC(HFNPCs.BARN_OWNER);
    private static final Script JUDGE_3 = new ContestJudgingScript(PREFIX, 3).setNPC(HFNPCs.BARN_OWNER);
    private static final Script JUDGE_4 = new ContestJudgingScript(PREFIX, 4).setNPC(HFNPCs.BARN_OWNER);
    private static final Script WINNER = new ContestWinningScript(PREFIX).setNPC(HFNPCs.BARN_OWNER);
    private static final BlockPos STAND1 = new BlockPos(8, 1, 12);
    private static final BlockPos STAND2 = new BlockPos(10, 1, 18);
    private static final BlockPos STAND3 = new BlockPos(27, 1, 16);
    private static final BlockPos STAND4 = new BlockPos(23, 1, 6);

    public QuestContestSheep() {
        super(HFNPCs.BARN_OWNER, PREFIX);
    }

    @Override
    protected AnimalContestEntries createEntries() {
        return new AnimalContestEntries<>(EntityHarvestSheep.class, STALLS, NPCS, NAMES);
    }

    @Override
    @Nonnull
    public ItemStack getReward(Place place) {
        switch (place) {
            case FIRST: {
                ItemStack stack = HFShops.getWoolyArmor(Items.LEATHER_CHESTPLATE, "Cashmere Sweater");
                if (stack.getTagCompound() != null) stack.getTagCompound().setLong(SELL_VALUE, 3000L);
                return stack;
            }
            case SECOND:
                return HFCooking.MEAL.getCreativeStack(Meal.STEW);
            default:
                return new ItemStack(Items.WHEAT);
        }
    }

    @Override
    public void execute(Town town, EntityPlayer player, NPCEntity npc) {
        npc.setPath(move(STAND1), speech(JUDGE_1), move(STAND2), speech(JUDGE_2), move(STAND3), speech(JUDGE_3), move(STAND4), speech(JUDGE_4),
                wait(1), speech(FINISH), move(PARK_SHEEP_JUDGE), speech(WINNER), new ContestTaskWinner(HFFestivals.SHEEP_FESTIVAL));
    }
}
