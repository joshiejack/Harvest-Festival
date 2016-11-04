package joshie.harvest.shops;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockSprinkler.Sprinkler;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.block.BlockFishing.FishingBlock;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockLadder.Ladder;
import joshie.harvest.mining.item.ItemMiningTool.MiningTool;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.shops.purchasable.*;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static joshie.harvest.animals.block.BlockSizedStorage.SizedStorage.INCUBATOR;
import static joshie.harvest.animals.block.BlockTray.Tray.FEEDER_EMPTY;
import static joshie.harvest.animals.block.BlockTray.Tray.NEST_EMPTY;
import static joshie.harvest.animals.block.BlockTrough.Trough.WOOD;
import static joshie.harvest.animals.item.ItemAnimalSpawner.Spawner.*;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.*;
import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.api.calendar.Weekday.*;
import static joshie.harvest.cooking.HFCooking.*;
import static joshie.harvest.cooking.block.BlockCookware.Cookware.*;
import static joshie.harvest.cooking.item.ItemIngredients.Ingredient.*;
import static joshie.harvest.cooking.item.ItemUtensil.Utensil.KNIFE;
import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.fishing.item.ItemFish.MEDIUM_FISH;
import static joshie.harvest.npc.item.ItemNPCTool.NPCTool.BLUE_FEATHER;

@HFLoader
public class HFShops {
    public static final IShop BARN = HFApi.shops.newShop(new ResourceLocation(MODID, "barn"), HFNPCs.BARN_OWNER);
    public static final IShop CAFE = HFApi.shops.newShop(new ResourceLocation(MODID, "cafe"), HFNPCs.CAFE_OWNER);
    public static final IShop KITCHEN = HFApi.shops.newShop(new ResourceLocation(MODID, "kitchen"), HFNPCs.CAFE_GRANNY);
    public static final IShop CARPENTER = HFApi.shops.newShop(new ResourceLocation(MODID, "carpenter"), HFNPCs.BUILDER);
    public static final IShop POULTRY = HFApi.shops.newShop(new ResourceLocation(MODID, "poultry"), HFNPCs.POULTRY);
    public static final IShop SUPERMARKET = HFApi.shops.newShop(new ResourceLocation(MODID, "general"), HFNPCs.GS_OWNER);
    public static final IShop MINER = HFApi.shops.newShop(new ResourceLocation(MODID, "miner"), HFNPCs.MINER);
    //Added in 0.6+
    public static final IShop BLOODMAGE = HFApi.shops.newShop(new ResourceLocation(MODID, "bloodmage"), HFNPCs.CLOCKMAKER);
    public static final IShop BAITSHOP = HFApi.shops.newShop(new ResourceLocation(MODID, "baitshop"), HFNPCs.FISHERMAN);

    public static void postInit() {
        registerBarn();
        registerBloodmage();
        registerCafe();
        registerCafeKitchen();
        registerCarpenter();
        registerMiner();
        registerPoultry();
        registerSupermarket();
        registerTackleshop();
    }
    
    private static void registerBarn() {
        BARN.addItem(800, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        BARN.addItem(2000, HFAnimals.TOOLS.getStackFromEnum(MILKER));
        BARN.addItem(1800, new ItemStack(Items.SHEARS));
        BARN.addItem(100, HFCrops.GRASS.getCropStack(1));
        BARN.addItem(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        BARN.addItem(new PurchasableEntity(EntityHarvestCow.class, 5000, HFAnimals.ANIMAL.getStackFromEnum(COW), true));
        BARN.addItem(new PurchasableEntity(EntityHarvestSheep.class, 4000, HFAnimals.ANIMAL.getStackFromEnum(SHEEP), true));
        BARN.addItem(3000, HFAnimals.TOOLS.getStackFromEnum(MIRACLE_POTION));
        BARN.addItem(500, HFAnimals.TROUGH.getStackFromEnum(WOOD));
        BARN.addItem(250, new ItemStack(Items.NAME_TAG));
        BARN.addItem(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        BARN.addItem(30, HFAnimals.TREATS.getStackFromEnum(Treat.COW));
        BARN.addItem(30, HFAnimals.TREATS.getStackFromEnum(Treat.SHEEP));

        BARN.addOpening(MONDAY, 10000, 15000).addOpening(TUESDAY, 10000, 15000).addOpening(WEDNESDAY, 10000, 15000);
        BARN.addOpening(THURSDAY, 10000, 15000).addOpening(FRIDAY, 10000, 15000).addOpening(SATURDAY, 10000, 15000);
    }

    private static void registerBloodmage() {
        BLOODMAGE.addItem(-30, new ItemStack(Items.SLIME_BALL));
        BLOODMAGE.addItem(-15, new ItemStack(Items.BONE));
        BLOODMAGE.addItem(-80, new ItemStack(Items.ENDER_PEARL));
        BLOODMAGE.addItem(-300, new ItemStack(Items.GOLDEN_APPLE));
        BLOODMAGE.addItem(-5, new ItemStack(Items.ROTTEN_FLESH));
        BLOODMAGE.addItem(-10, new ItemStack(Items.SPIDER_EYE));
        BLOODMAGE.addItem(-30, new ItemStack(Items.POISONOUS_POTATO));
        BLOODMAGE.addItem(-60, new ItemStack(Items.GHAST_TEAR));
        BLOODMAGE.addItem(-20, new ItemStack(Items.FERMENTED_SPIDER_EYE));
        BLOODMAGE.addItem(-70, new ItemStack(Items.BLAZE_ROD));
        BLOODMAGE.addItem(-35, new ItemStack(Items.MAGMA_CREAM));
        BLOODMAGE.addItem(-30, new ItemStack(Items.SPECKLED_MELON));
        BLOODMAGE.addItem(-150, new ItemStack(Items.GOLDEN_CARROT));
        BLOODMAGE.addItem(-100, new ItemStack(Items.RABBIT_FOOT));
        BLOODMAGE.addItem(-40, new ItemStack(Items.GUNPOWDER));
        BLOODMAGE.addItem(-5, new ItemStack(Items.REDSTONE));
        BLOODMAGE.addItem(-10, new ItemStack(Items.GLOWSTONE_DUST));
        BLOODMAGE.addOpening(WEDNESDAY, 19000, 24000).addOpening(WEDNESDAY, 0, 5000).addOpening(SATURDAY, 18000, 24000).addOpening(SATURDAY, 0, 3500);
    }

    private static void registerCafe() {
        CAFE.addItem(0, new ItemStack(Items.POTIONITEM));
        CAFE.addItem(new PurchasableMeal(200, new ResourceLocation(MODID, "salad")));
        CAFE.addItem(new PurchasableMeal(100, new ResourceLocation(MODID, "cookies")));
        CAFE.addItem(new PurchasableMeal(250, new ResourceLocation(MODID, "juice_pineapple")));
        CAFE.addItem(new PurchasableMeal(250, new ResourceLocation(MODID, "corn_baked")));
        CAFE.addItem(new PurchasableMeal(300, new ResourceLocation(MODID, "ice_cream")));

        //Allow the purchasing of cookware at the weekends
        CAFE.addItem(new PurchasableWeekend(25, new ItemStack(COOKBOOK)));
        CAFE.addItem(new PurchasableWeekend(50, UTENSILS.getStackFromEnum(KNIFE)));
        CAFE.addItem(new PurchasableWeekend(250, COOKWARE.getStackFromEnum(COUNTER)));
        CAFE.addItem(new PurchasableWeekend(3000, COOKWARE.getStackFromEnum(FRIDGE)));
        CAFE.addItem(new PurchasableWeekend(2500, COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addItem(new PurchasableWeekend(1500, COOKWARE.getStackFromEnum(FRYING_PAN), COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addItem(new PurchasableWeekend(1000, COOKWARE.getStackFromEnum(POT), COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addItem(new PurchasableWeekend(1200, COOKWARE.getStackFromEnum(MIXER), COOKWARE.getStackFromEnum(COUNTER)));

        //Add recipes for purchase
        CAFE.addItem(new PurchasableRecipe(SPRING, MONDAY, new ResourceLocation(MODID, "juice_vegetable")));
        CAFE.addItem(new PurchasableRecipe(SPRING, TUESDAY, new ResourceLocation(MODID, "sushi")));
        CAFE.addItem(new PurchasableRecipe(SPRING, WEDNESDAY, new ResourceLocation(MODID, "sashimi")));
        CAFE.addItem(new PurchasableRecipe(SPRING, THURSDAY, new ResourceLocation(MODID, "sashimi_chirashi")));
        CAFE.addItem(new PurchasableRecipe(SPRING, FRIDAY, new ResourceLocation(MODID, "cucumber_pickled")));
        CAFE.addItem(new PurchasableRecipe(SUMMER, SATURDAY, new ResourceLocation(MODID, "juice_tomato")));
        CAFE.addItem(new PurchasableRecipe(SUMMER, SUNDAY, new ResourceLocation(MODID, "cornflakes")));
        CAFE.addItem(new PurchasableRecipe(SUMMER, MONDAY, new ResourceLocation(MODID, "ketchup")));
        CAFE.addItem(new PurchasableRecipe(SUMMER, TUESDAY, new ResourceLocation(MODID, "stew_pumpkin")));
        CAFE.addItem(new PurchasableRecipe(SUMMER, THURSDAY, new ResourceLocation(MODID, "doria")));
        CAFE.addItem(new PurchasableRecipe(AUTUMN, TUESDAY, new ResourceLocation(MODID, "eggplant_happy")));
        CAFE.addItem(new PurchasableRecipe(AUTUMN, WEDNESDAY, new ResourceLocation(MODID, "sandwich")));
        CAFE.addItem(new PurchasableRecipe(AUTUMN, SATURDAY, new ResourceLocation(MODID, "spinach_boiled")));
        CAFE.addItem(new PurchasableRecipe(AUTUMN, SUNDAY, new ResourceLocation(MODID, "riceballs_toasted")));
        CAFE.addItem(new PurchasableRecipe(WINTER, MONDAY, new ResourceLocation(MODID, "omelet")));
        CAFE.addItem(new PurchasableRecipe(WINTER, TUESDAY, new ResourceLocation(MODID, "egg_boiled")));
        CAFE.addItem(new PurchasableRecipe(WINTER, WEDNESDAY, new ResourceLocation(MODID, "egg_overrice")));
        CAFE.addItem(new PurchasableRecipe(WINTER, FRIDAY, new ResourceLocation(MODID, "pancake")));

        CAFE.addOpening(MONDAY, 9500, 17000).addOpening(TUESDAY, 9500, 17000).addOpening(WEDNESDAY, 9500, 17000).addOpening(THURSDAY, 9500, 17000);
        CAFE.addOpening(FRIDAY, 9500, 17000).addOpening(SATURDAY, 9500, 17000).addOpening(SUNDAY, 9500, 17000);
    }

    private static void registerCafeKitchen() {
        KITCHEN.addItem(-50, new ItemStack(Blocks.BROWN_MUSHROOM));
        KITCHEN.addItem(-60, new ItemStack(Items.PORKCHOP));
        KITCHEN.addItem(-80, new ItemStack(Items.COOKED_PORKCHOP));
        KITCHEN.addItem(-400, new ItemStack(Items.CAKE));
        KITCHEN.addItem(-250, new ItemStack(Items.COOKIE));
        KITCHEN.addItem(-60, new ItemStack(Items.BEEF));
        KITCHEN.addItem(-80, new ItemStack(Items.COOKED_BEEF));
        KITCHEN.addItem(-40, new ItemStack(Items.CHICKEN));
        KITCHEN.addItem(-50, new ItemStack(Items.COOKED_CHICKEN));
        KITCHEN.addItem(-90, new ItemStack(Items.BAKED_POTATO));
        KITCHEN.addItem(-300, new ItemStack(Items.PUMPKIN_PIE));
        KITCHEN.addItem(-40, new ItemStack(Items.RABBIT));
        KITCHEN.addItem(-50, new ItemStack(Items.COOKED_RABBIT));
        KITCHEN.addItem(-250, new ItemStack(Items.RABBIT_FOOT));
        KITCHEN.addItem(-80, new ItemStack(Items.MUSHROOM_STEW));
        KITCHEN.addItem(-80, new ItemStack(Items.MUTTON));
        KITCHEN.addItem(-100, new ItemStack(Items.COOKED_MUTTON));
        KITCHEN.addItem(-500, new ItemStack(Items.BEETROOT_SOUP));
        KITCHEN.addItem(-10, new ItemStack(Items.SUGAR));
        KITCHEN.addItem(-25, new ItemStack(Items.CHORUS_FRUIT));
        KITCHEN.addOpening(FRIDAY, 6000, 9500).addOpening(FRIDAY, 17000, 20000);
        HFNPCs.CAFE_GRANNY.setHasInfo(null, null); //Remove the opening times from the granny, it's a bonus
    }

    private static void registerCarpenter() {
        for (BuildingImpl building : BuildingRegistry.REGISTRY.getValues()) {
            if (building.canPurchase()) {
                CARPENTER.addItem(new PurchasableBuilding(building));
            }
        }

        CARPENTER.addItem(new PurchasableBuilder(0, 16, 0, HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING)));
        CARPENTER.addItem(new PurchasableBuilder(10000L, 24, 6, HFCrops.SPRINKLER.getStackFromEnum(Sprinkler.WOOD)) {
            @Override
            public boolean canBuy(World world, EntityPlayer player) {
                return HFApi.calendar.getDate(world).getWeekday().isWeekend() && CalendarHelper.haveYearsPassed(world, player, 1);
            }

        });

        CARPENTER.addItem(new PurchasableBuilder(10, 0, 0, new ItemStack(Blocks.LOG)));
        CARPENTER.addItem(new PurchasableBuilder(20, 0, 0, new ItemStack(Blocks.STONE)));
        if (!HFAnimals.CAN_SPAWN) {
            CARPENTER.addItem(new PurchasableBuilder(500, 3, 0, new ItemStack(Items.BED)));
        }

        //Selling things to the carpenter
        CARPENTER.addItem(-5, new ItemStack(Blocks.LOG));
        CARPENTER.addItem(-10, new ItemStack(Blocks.STONE));

        CARPENTER.addOpening(MONDAY, 9000, 17500).addOpening(TUESDAY, 9000, 17500).addOpening(WEDNESDAY, 9000, 17500);
        CARPENTER.addOpening(THURSDAY, 9000, 17500).addOpening(FRIDAY, 9000, 17500).addOpening(SUNDAY, 9000, 17500);
    }

    private static void registerMiner() {
        MINER.addItem(1000, HFMining.LADDER.getStackFromEnum(Ladder.DECORATIVE));
        MINER.addItem(250, HFMining.MINING_TOOL.getStackFromEnum(MiningTool.ESCAPE_ROPE));
        //Selling things to the mine
        MINER.addItem(-5, new ItemStack(Items.COAL, 1, 1));
        MINER.addItem(-8, new ItemStack(Items.GOLD_NUGGET));
        MINER.addItem(-10, new ItemStack(Items.COAL));
        MINER.addItem(-15, new ItemStack(Items.QUARTZ));
        MINER.addItem(-45, new ItemStack(Items.IRON_INGOT));
        MINER.addItem(-72, new ItemStack(Items.GOLD_INGOT));

        MINER.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        MINER.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }

    private static void registerPoultry() {
        POULTRY.addItem(50, HFAnimals.TOOLS.getStackFromEnum(CHICKEN_FEED));
        POULTRY.addItem(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        POULTRY.addItem(new PurchasableEntity(EntityHarvestChicken.class, 1500, HFAnimals.ANIMAL.getStackFromEnum(CHICKEN), false));
        POULTRY.addItem(250, new ItemStack(Items.NAME_TAG));
        POULTRY.addItem(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        POULTRY.addItem(30, HFAnimals.TREATS.getStackFromEnum(Treat.CHICKEN));
        POULTRY.addItem(500, HFAnimals.TRAY.getStackFromEnum(FEEDER_EMPTY));
        POULTRY.addItem(1000, HFAnimals.TRAY.getStackFromEnum(NEST_EMPTY));
        POULTRY.addItem(7500, HFAnimals.SIZED.getStackFromEnum(INCUBATOR));

        POULTRY.addOpening(MONDAY, 5000, 11000).addOpening(TUESDAY, 5000, 11000).addOpening(WEDNESDAY, 5000, 11000);
        POULTRY.addOpening(THURSDAY, 5000, 11000).addOpening(FRIDAY, 5000, 11000).addOpening(SATURDAY, 5000, 11000);
    }

    private static void registerSupermarket() {
        SUPERMARKET.addItem(250, HFTools.HOE.getStack(ToolTier.BASIC));
        SUPERMARKET.addItem(250, HFTools.SICKLE.getStack(ToolTier.BASIC));
        SUPERMARKET.addItem(500, HFTools.WATERING_CAN.getStack(ToolTier.BASIC));
        SUPERMARKET.addItem(1000, HFTools.AXE.getStack(ToolTier.BASIC));
        SUPERMARKET.addItem(1000, HFTools.HAMMER.getStack(ToolTier.BASIC));

        for (Crop crop : Crop.REGISTRY.getValues()) {
            if (crop != Crop.NULL_CROP) {
                SUPERMARKET.addItem(new PurchasableCropSeeds(crop));
            }
        }

        SUPERMARKET.addItem(new PurchasableBlueFeather(1000, HFNPCs.TOOLS.getStackFromEnum(BLUE_FEATHER)));
        SUPERMARKET.addItem(RICEBALL.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(RICEBALL));
        SUPERMARKET.addItem(100, new ItemStack(Items.BREAD));
        SUPERMARKET.addItem(OIL.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(OIL));
        SUPERMARKET.addItem(FLOUR.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(FLOUR));
        SUPERMARKET.addItem(CURRY_POWDER.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(CURRY_POWDER));
        SUPERMARKET.addItem(DUMPLING_POWDER.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(DUMPLING_POWDER));
        SUPERMARKET.addItem(CHOCOLATE.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(CHOCOLATE));
        SUPERMARKET.addItem(WINE.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(WINE));
        SUPERMARKET.addItem(SALT.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(SALT));

        SUPERMARKET.addOpening(MONDAY, 9000, 17000).addOpening(TUESDAY, 9000, 17000).addOpening(THURSDAY, 9000, 17000);
        SUPERMARKET.addOpening(FRIDAY, 9000, 17000).addOpening(SATURDAY, 11000, 15000);
    }

    private static void registerTackleshop() {
        BAITSHOP.addItem(Junk.BAIT.getCost(), HFFishing.JUNK.getStackFromEnum(Junk.BAIT));
        BAITSHOP.addItem(1000L, HFFishing.FISHING_ROD.getStack(ToolTier.BASIC));
        BAITSHOP.addItem(1500L, HFFishing.FISHING_BLOCK.getStackFromEnum(FishingBlock.TRAP));
        BAITSHOP.addItem(3000L, HFFishing.FISHING_BLOCK.getStackFromEnum(FishingBlock.HATCHERY));

        //Selling things to the carpenter
        BAITSHOP.addItem(-100, new ItemStack(Items.PRISMARINE_SHARD));
        BAITSHOP.addItem(-150, new ItemStack(Items.PRISMARINE_CRYSTALS));
        BAITSHOP.addItem(-10, new ItemStack(Items.FISH, 1, 0));
        BAITSHOP.addItem(-30, new ItemStack(Items.FISH, 1, 1));
        BAITSHOP.addItem(-50, new ItemStack(Items.FISH, 1, 2));
        BAITSHOP.addItem(-100, new ItemStack(Items.FISH, 1, 3));
        for (Fish fish: Fish.values()) {
            long sell = (long)-Math.floor(fish.getSellValue(fish.getLengthFromSizeOfFish(MEDIUM_FISH)));
            BAITSHOP.addItem(sell, HFFishing.FISH.getStackFromEnum(fish));
        }

        BAITSHOP.addOpening(TUESDAY, 13000, 19000).addOpening(WEDNESDAY, 13000, 19000).addOpening(THURSDAY, 13000, 19000).addOpening(FRIDAY, 13000, 19000);
    }

    public static boolean TWENTY_FOUR_HOUR_SHOPPING;

    public static void configure() {
        TWENTY_FOUR_HOUR_SHOPPING = getBoolean("Shops are open all the time", false);
    }
}