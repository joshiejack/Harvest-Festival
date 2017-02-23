package joshie.harvest.quests.town.festivals;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.quests.town.festivals.contest.ContestEntries;
import joshie.harvest.quests.town.festivals.cow.CowContestPathing;
import joshie.harvest.town.BuildingLocations;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@HFQuest("festival.cow")
public class QuestContestCow extends QuestAnimalContest<EntityHarvestCow> {
    private static final BuildingLocation[] LOCATIONS = new BuildingLocation[] { BuildingLocations.PARK_COW_STALL_1, BuildingLocations.PARK_COW_STALL_2, BuildingLocations.PARK_CENTRE, BuildingLocations.PARK_COW_STALL_4 };
    private static final NPC[] NPCS = new NPC[] { HFNPCs.BARN_OWNER, HFNPCs.GS_OWNER, HFNPCs.TRADER, HFNPCs.CARPENTER };
    private static final String[] NAMES = new String[] { "Anabelle", "Maybelle", "Daisy", "Miltank", "Bessie", "Clarabelle", "Dorothy", "Ella", "Molly", "Bella"};

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
    public void execute(EntityPlayer player, EntityAIPathing pathing) {
        CowContestPathing.execute(player, this, pathing);
    }
}
