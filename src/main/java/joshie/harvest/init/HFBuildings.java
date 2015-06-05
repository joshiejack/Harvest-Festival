package joshie.harvest.init;

import joshie.harvest.animals.AnimalType;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingBarn;
import joshie.harvest.buildings.BuildingBlacksmith;
import joshie.harvest.buildings.BuildingCafe;
import joshie.harvest.buildings.BuildingCarpenter;
import joshie.harvest.buildings.BuildingChurch;
import joshie.harvest.buildings.BuildingClockmaker;
import joshie.harvest.buildings.BuildingFishingHole;
import joshie.harvest.buildings.BuildingFishingHut;
import joshie.harvest.buildings.BuildingGoddess;
import joshie.harvest.buildings.BuildingMiningHill;
import joshie.harvest.buildings.BuildingMiningHut;
import joshie.harvest.buildings.BuildingPoultryFarm;
import joshie.harvest.buildings.BuildingSupermarket;
import joshie.harvest.buildings.BuildingTownhall;
import joshie.harvest.core.lib.LootStrings;
import joshie.harvest.crops.Crop;
import joshie.harvest.items.ItemGeneral;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class HFBuildings {
    public static Building barn;
    public static Building blacksmith;
    public static Building cafe;
    public static Building carpenter;
    public static Building church;
    public static Building clockmaker;
    public static Building fishingHole;
    public static Building fishingHut;
    public static Building goddessPond;
    public static Building miningHill;
    public static Building miningHut;
    public static Building poultryFarm;
    public static Building supermarket;
    public static Building townhall;

    public static void preInit() {
        barn = new BuildingBarn().init();
        blacksmith = new BuildingBlacksmith().init();
        cafe = new BuildingCafe().init();
        carpenter = new BuildingCarpenter().init();
        church = new BuildingChurch().init();
        clockmaker = new BuildingClockmaker().init();
        fishingHole = new BuildingFishingHole().init();
        fishingHut = new BuildingFishingHut().init();
        goddessPond = new BuildingGoddess().init();
        miningHill = new BuildingMiningHill().init();
        miningHut = new BuildingMiningHut().init();
        poultryFarm = new BuildingPoultryFarm().init();
        supermarket = new BuildingSupermarket().init();
        townhall = new BuildingTownhall().init();
    }

    public static void init() {
        //Barn Frame
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(Items.wheat), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(Items.carrot), 3, 7, 10));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(Items.lead), 1, 1, 8));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(Items.carrot_on_a_stick), 1, 1, 1));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.BRUSH), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.MEDICINE), 1, 3, 1));
        ChestGenHooks.addItem(LootStrings.BARN_FRAME, new WeightedRandomChestContent(HFCrops.grass.getCropStack(), 1, 1, 6));

        //Blacksmith Frame
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.iron_axe), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.iron_sword), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.iron_shovel), 1, 1, 6));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.iron_pickaxe), 1, 1, 1));

        //Blacksmith Chest
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_CHEST, new WeightedRandomChestContent(new ItemStack(Items.iron_ingot), 2, 5, 4));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_CHEST, new WeightedRandomChestContent(new ItemStack(Items.lava_bucket), 1, 2, 2));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_CHEST, new WeightedRandomChestContent(new ItemStack(Items.coal), 3, 33, 6));
        ChestGenHooks.addItem(LootStrings.BLACKSMITH_CHEST, new WeightedRandomChestContent(new ItemStack(Items.leather), 13, 64, 10));

        //Church Frame
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.book), 3, 9, 10));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.gold_ingot), 1, 7, 2));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.gold_nugget), 1, 15, 8));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.experience_bottle), 1, 7, 10));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.speckled_melon), 1, 3, 3));
        ChestGenHooks.addItem(LootStrings.CHURCH_FRAME, new WeightedRandomChestContent(new ItemStack(Items.potionitem, 1, 8193), 1, 1, 1));

        //Fishing Frame
        ChestGenHooks.addItem(LootStrings.FISHING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.fishing_rod), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.FISHING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.leather_boots), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.FISHING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.potionitem, 1, 8269), 1, 1, 1));

        //Fishing Chest
        ChestGenHooks.addItem(LootStrings.FISHING_CHEST, new WeightedRandomChestContent(new ItemStack(Items.fish, 1, 0), 3, 33, 10));
        ChestGenHooks.addItem(LootStrings.FISHING_CHEST, new WeightedRandomChestContent(new ItemStack(Items.fish, 1, 1), 2, 22, 10));
        ChestGenHooks.addItem(LootStrings.FISHING_CHEST, new WeightedRandomChestContent(new ItemStack(Items.fish, 1, 3), 1, 11, 5));
        ChestGenHooks.addItem(LootStrings.FISHING_CHEST, new WeightedRandomChestContent(new ItemStack(Items.fish, 1, 2), 1, 3, 1));

        //Poultry Frame
        ChestGenHooks.addItem(LootStrings.POULTRY_FRAME, new WeightedRandomChestContent(HFCrops.wheat.getCropStack(), 3, 9, 5));
        ChestGenHooks.addItem(LootStrings.POULTRY_FRAME, new WeightedRandomChestContent(HFCrops.wheat.getSeedStack(), 1, 3, 1));

        //Poultry Chest
        ChestGenHooks.addItem(LootStrings.POULTRY_CHEST, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.CHICKEN_FEED), 8, 24, 10));
        ChestGenHooks.addItem(LootStrings.POULTRY_CHEST, new WeightedRandomChestContent(new ItemStack(HFItems.treats, 1, AnimalType.CHICKEN.ordinal()), 1, 2, 5));
        ChestGenHooks.addItem(LootStrings.POULTRY_CHEST, new WeightedRandomChestContent(new ItemStack(HFItems.egg, 1, 0), 1, 2, 5));

        //Mining Frame
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.stone_pickaxe), 1, 1, 20));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.iron_pickaxe), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Items.golden_pickaxe), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.torch), 7, 21, 10));
        ChestGenHooks.addItem(LootStrings.MINING_FRAME, new WeightedRandomChestContent(new ItemStack(Blocks.tnt), 2, 3, 4));

        //Mining Chest
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.iron_ore), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.diamond_ore), 1, 1, 1));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.gold_ore), 1, 2, 3));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.coal_ore), 2, 7, 10));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.lapis_ore), 1, 3, 5));
        ChestGenHooks.addItem(LootStrings.MINING_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.stone), 7, 21, 10));

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
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(HFCrops.pineapple.getCropStack(), 1, 1, 1));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(HFCrops.strawberry.getCropStack(), 2, 3, 5));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.apple), 1, 3, 10));
        ChestGenHooks.addItem(LootStrings.TOWNHALL_TEENAGER_CHEST, new WeightedRandomChestContent(new ItemStack(Items.melon), 7, 11, 8));

        //Market Entry
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.turnip.getSeedStack(), 1, 1, 10));
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.potato.getSeedStack(), 1, 1, 5));
        ChestGenHooks.addItem(LootStrings.MARKET_ENTRY_FRAME, new WeightedRandomChestContent(HFCrops.cucumber.getSeedStack(), 1, 1, 1));

        //Market Bedroom Frame
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.book), 1, 1, 3));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.dye, 1, 0), 2, 3, 10));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_FRAME, new WeightedRandomChestContent(new ItemStack(Items.paper), 2, 3, 5));

        //Market Bedroom Chests
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.melon), 3, 7, 7));
        ChestGenHooks.addItem(LootStrings.MARKET_BEDROOM_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 10));

        //Market Basement Chests
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(Items.sugar), 3, 7, 9));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.SALT), 2, 3, 8));
        ChestGenHooks.addItem(LootStrings.MARKET_BASEMENT_CHESTS, new WeightedRandomChestContent(new ItemStack(HFItems.general, 1, ItemGeneral.CHOCOLATE), 1, 3, 7));
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
        for (ICrop crop : Crop.crops) {
            ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(crop.getSeedStack(), 1, 1, 3));
            ChestGenHooks.addItem(LootStrings.JADE_CHEST, new WeightedRandomChestContent(crop.getCropStack(), 2, 3, 5));
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
