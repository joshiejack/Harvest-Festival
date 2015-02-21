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
