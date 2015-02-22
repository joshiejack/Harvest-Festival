package joshie.harvestmoon.init;

import joshie.harvestmoon.buildings.BuildingBarn;
import joshie.harvestmoon.buildings.BuildingBlacksmith;
import joshie.harvestmoon.buildings.BuildingCafe;
import joshie.harvestmoon.buildings.BuildingCarpenter;
import joshie.harvestmoon.buildings.BuildingChurch;
import joshie.harvestmoon.buildings.BuildingClockmaker;
import joshie.harvestmoon.buildings.BuildingFishingHole;
import joshie.harvestmoon.buildings.BuildingFishingHut;
import joshie.harvestmoon.buildings.BuildingGoddess;
import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.buildings.BuildingMiningHill;
import joshie.harvestmoon.buildings.BuildingMiningHut;
import joshie.harvestmoon.buildings.BuildingPoultryFarm;
import joshie.harvestmoon.buildings.BuildingSupermarket;
import joshie.harvestmoon.buildings.BuildingTownhall;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.items.ItemGeneral;
import joshie.harvestmoon.lib.LootStrings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class HMBuildings {
    public static BuildingGroup barn;
    public static BuildingGroup blacksmith;
    public static BuildingGroup cafe;
    public static BuildingGroup carpenter;
    public static BuildingGroup church;
    public static BuildingGroup clockmaker;
    public static BuildingGroup fishingHole;
    public static BuildingGroup fishingHut;
    public static BuildingGroup goddessPond;
    public static BuildingGroup miningHill;
    public static BuildingGroup miningHut;
    public static BuildingGroup poultryFarm;
    public static BuildingGroup supermarket;
    public static BuildingGroup townhall;

    public static void init() {
        barn = BuildingGroup.register("barn", new BuildingBarn());
        blacksmith = BuildingGroup.register("blacksmith", new BuildingBlacksmith());
        cafe = BuildingGroup.register("cafe", new BuildingCafe());
        carpenter = BuildingGroup.register("carpenter", new BuildingCarpenter());
        church = BuildingGroup.register("church", new BuildingChurch());
        clockmaker = BuildingGroup.register("clockmaker", new BuildingClockmaker());
        fishingHole = BuildingGroup.register("fishingHole", new BuildingFishingHole());
        fishingHut = BuildingGroup.register("fishingHut", new BuildingFishingHut());
        goddessPond = BuildingGroup.register("goddessPond", new BuildingGoddess());
        miningHill = BuildingGroup.register("miningHill", new BuildingMiningHill());
        miningHut = BuildingGroup.register("miningHut", new BuildingMiningHut());
        poultryFarm = BuildingGroup.register("poultryFarm", new BuildingPoultryFarm());
        supermarket = BuildingGroup.register("supermarket", new BuildingSupermarket());
        townhall = BuildingGroup.register("townhall", new BuildingTownhall());

        //Cafe Frame
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.mushroom_stew), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.cooked_beef), 1, 3, 15));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.cake), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.pumpkin_pie), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.baked_potato), 1, 2, 6));
        ChestGenHooks.addItem(LootStrings.CAFE_FRAME, new WeightedRandomChestContent(new ItemStack(Items.bread), 2, 3, 7));

        //Cafe Chest
        ChestGenHooks.addItem(LootStrings.CAFE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.sugar), 7, 21, 10));
        ChestGenHooks.addItem(LootStrings.CAFE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.apple), 5, 11, 8));
        ChestGenHooks.addItem(LootStrings.CAFE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.bowl), 10, 20, 4));
        ChestGenHooks.addItem(LootStrings.CAFE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.dye, 1, 3), 2, 3, 5));

        //Townhall Hall Frame
        ChestGenHooks.addItem(LootStrings.TOWNHALL_HALL_FRAME, new WeightedRandomChestContent(new ItemStack(Items.paper), 2, 5, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_HALL_FRAME, new WeightedRandomChestContent(new ItemStack(Items.sign), 2, 5, 10));

        //Townhall Priest Frame
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.writable_book), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.book), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.dye, 1, 0), 3, 9, 7));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.emerald), 1, 3, 3));

        //Townhall Mayor Frame
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.potionitem, 1, 8201), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.potionitem, 1, 8233), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_MASTER_MAYOR_FRAME, new WeightedRandomChestContent(new ItemStack(Items.potionitem, 1, 8265), 1, 1, 7));

        //Townhall Child Frame
        ChestGenHooks.addItem(LootStrings.TOWNHALL_CHILD_FRAME, new WeightedRandomChestContent(new ItemStack(Items.saddle), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_CHILD_FRAME, new WeightedRandomChestContent(new ItemStack(Items.sugar), 2, 5, 15));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_CHILD_FRAME, new WeightedRandomChestContent(new ItemStack(Items.carrot), 2, 3, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_CHILD_FRAME, new WeightedRandomChestContent(new ItemStack(Items.iron_horse_armor), 1, 1, 3));

        //Townhall Teenager Frame
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.book), 2, 5, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.dye, 1, 0), 7, 15, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.paper), 6, 11, 5));

        //Townhall Passage Chest
        ChestGenHooks.addItem(LootStrings.TOWNHALL_PASSAGE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.web), 1, 1, 5));
        for (int i = 0; i < 16; i++) {
            ChestGenHooks.addItem(LootStrings.TOWNHALL_PASSAGE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.carpet, 1, i), 2, 7, 8));
        }

        for (int i = 0; i < 4; i++) {
            ChestGenHooks.addItem(LootStrings.TOWNHALL_PASSAGE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.leaves, 1, i), 3, 11, 4));
            ChestGenHooks.addItem(LootStrings.TOWNHALL_PASSAGE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.planks, 1, i), 5, 24, 10));
        }

        //Townhall Teenager Chest
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(new ItemStack(HMItems.crops, 1, HMCrops.pineapple.getCropMeta()), 1, 1, 1));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(new ItemStack(HMItems.crops, 1, HMCrops.strawberry.getCropMeta()), 2, 3, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.melon), 7, 11, 8));

        //Market Entry
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(new ItemStack(HMItems.seeds, 1, HMCrops.turnip.getCropMeta()), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(new ItemStack(HMItems.seeds, 1, HMCrops.potato.getCropMeta()), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(new ItemStack(HMItems.seeds, 1, HMCrops.cucumber.getCropMeta()), 1, 1, 1));

        //Market Bedroom Frame
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.book), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.dye, 1, 0), 2, 3, 10));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.paper), 2, 3, 5));

        //Market Bedroom Chests
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.melon), 3, 7, 7));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 10));

        //Market Basement Chests
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.sugar), 3, 7, 9));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(HMItems.general, 1, ItemGeneral.SALT), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(HMItems.general, 1, ItemGeneral.CHOCOLATE), 1, 3, 7));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.bowl), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.dye, 1, 15), 3, 7, 7));

        //Item Frame generations for Clockmaker
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.redstone), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 3, 2));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.clock), 1, 1, 5));

        //Chest for Clockmaker
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.cooked_beef), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.cooked_chicken), 2, 3, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.cooked_porkchop), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.CLOCKMAKER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.baked_potato), 1, 1, 10));

        //Item Frame generations for Carpenter
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.stick), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Items.stone_axe), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.sapling, 1, 5), 1, 3, 2));
        ChestGenHooks.addItem(LootStrings.CARPENTER_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.wooden_slab), 1, 1, 5));

        //Chest generations for Jade Chest
        ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.carrot), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.red_flower), 1, 3, 15));
        ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.yellow_flower), 1, 2, 10));
        for (Crop crop : Crop.crops) {
            ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(HMItems.seeds, 1, crop.getCropMeta()), 1, 1, 3));
            ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(HMItems.crops, 1, crop.getCropMeta()), 2, 3, 5));
        }

        //Chest generations for Yulif Chest
        ChestGenHooks.addItem(LootStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.porkchop), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.beef), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.iron_axe), 1, 1, 3));
        for (int i = 0; i < 4; i++) {
            ChestGenHooks.addItem(LootStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.log, 1, i), 1, 5, 10));
        }
    }
}
