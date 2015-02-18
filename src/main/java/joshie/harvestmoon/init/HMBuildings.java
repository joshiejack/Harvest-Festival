package joshie.harvestmoon.init;

import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.buildings.barn.BuildingBarn;
import joshie.harvestmoon.buildings.blacksmith.BuildingBlacksmith;
import joshie.harvestmoon.buildings.carpenter.BuildingCarpenter;
import joshie.harvestmoon.buildings.church.BuildingChurch;
import joshie.harvestmoon.buildings.clockmaker.BuildingClockmaker;
import joshie.harvestmoon.buildings.fishing.BuildingFishingHole;
import joshie.harvestmoon.buildings.fishing.BuildingFishingHut;
import joshie.harvestmoon.buildings.goddess.BuildingGoddess;
import joshie.harvestmoon.buildings.miner.BuildingMiningHill;
import joshie.harvestmoon.buildings.miner.BuildingMiningHut;
import joshie.harvestmoon.buildings.poultry.BuildingPoultryFarm;
import joshie.harvestmoon.buildings.supermarket.BuildingSupermarket;
import joshie.harvestmoon.buildings.townhall.BuildingTownhall;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.lib.VillageStrings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class HMBuildings {
    public static BuildingGroup barn;
    public static BuildingGroup blacksmith;
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

    //TODO: Fix Furnace Rotations, Ladders
    public static void init() {
        barn = BuildingGroup.register("barn", new BuildingBarn());
        blacksmith = BuildingGroup.register("blacksmith", new BuildingBlacksmith());
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

        //Chest generations for Jade Chest
        ChestGenHooks.addItem(VillageStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 1, 8));
        ChestGenHooks.addItem(VillageStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.carrot), 1, 1, 8));
        ChestGenHooks.addItem(VillageStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.red_flower), 1, 3, 20));
        ChestGenHooks.addItem(VillageStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.yellow_flower), 1, 2, 10));
        for (Crop crop : Crop.crops) {
            ChestGenHooks.addItem(VillageStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(HMItems.seeds, 1, crop.getCropMeta()), 1, 1, 3));
            ChestGenHooks.addItem(VillageStrings.JADE_CHEST, new WeightedRandomChestContent(new ItemStack(HMItems.crops, 1, crop.getCropMeta()), 2, 3, 5));
        }

        //Chest generations for Yulif Chest
        ChestGenHooks.addItem(VillageStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.porkchop), 1, 1, 8));
        ChestGenHooks.addItem(VillageStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.beef), 1, 1, 8));
        ChestGenHooks.addItem(VillageStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Items.iron_axe), 1, 1, 3));
        ChestGenHooks.addItem(VillageStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.sapling, 1, 1), 1, 3, 20));
        ChestGenHooks.addItem(VillageStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.sapling, 1, 5), 1, 1, 5));
        for (int i = 0; i < 4; i++) {
            ChestGenHooks.addItem(VillageStrings.YULIF_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.log, 1, i), 1, 5, 10));
        }
    }
}
