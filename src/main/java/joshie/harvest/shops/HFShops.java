package joshie.harvest.shops;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.buildings.HFBuildings;
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
import joshie.harvest.quests.Quests;
import joshie.harvest.shops.purchasable.*;
import joshie.harvest.shops.requirement.*;
import joshie.harvest.shops.rules.SpecialRulesFriendship;
import joshie.harvest.tools.HFTools;
import joshie.harvest.town.TownHelper;
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
    public static final IShop CARPENTER = HFApi.shops.newShop(new ResourceLocation(MODID, "carpenter"), HFNPCs.BUILDER);
    public static final IShop POULTRY = HFApi.shops.newShop(new ResourceLocation(MODID, "poultry"), HFNPCs.POULTRY);
    public static final IShop SUPERMARKET = HFApi.shops.newShop(new ResourceLocation(MODID, "general"), HFNPCs.GS_OWNER);
    public static final IShop MINER = HFApi.shops.newShop(new ResourceLocation(MODID, "miner"), HFNPCs.MINER);
    //Added in 0.6+
    public static final IShop BAITSHOP = HFApi.shops.newShop(new ResourceLocation(MODID, "baitshop"), HFNPCs.FISHERMAN);
    public static final IShop BLOODMAGE = HFApi.shops.newShop(new ResourceLocation(MODID, "bloodmage"), HFNPCs.CLOCKMAKER).setSpecialPurchaseRules(new SpecialRulesFriendship(HFNPCs.CLOCKMAKER, 15000));
    public static final IShop KITCHEN = HFApi.shops.newShop(new ResourceLocation(MODID, "kitchen"), HFNPCs.CAFE_GRANNY).setSpecialPurchaseRules(new SpecialRulesFriendship(HFNPCs.CAFE_GRANNY, 15000));
    public static final IShop TRADER = HFApi.shops.newShop(new ResourceLocation(MODID, "trader"), HFNPCs.TRADER).setSpecialPurchaseRules(new SpecialRulesFriendship(HFNPCs.TRADER, 15000));

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
        registerTrader();
    }
    
    private static void registerBarn() {
        BARN.addPurchasable(100, HFCrops.GRASS.getCropStack(1));
        BARN.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        BARN.addPurchasable(new PurchasableEntity(EntityHarvestCow.class, 5000, HFAnimals.ANIMAL.getStackFromEnum(COW), true));
        BARN.addPurchasable(new PurchasableEntity(EntityHarvestSheep.class, 4000, HFAnimals.ANIMAL.getStackFromEnum(SHEEP), true));
        BARN.addPurchasable(3000, HFAnimals.TOOLS.getStackFromEnum(MIRACLE_POTION));
        BARN.addPurchasable(500, HFAnimals.TROUGH.getStackFromEnum(WOOD));
        BARN.addPurchasable(250, new ItemStack(Items.NAME_TAG));
        BARN.addPurchasable(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        BARN.addPurchasable(30, HFAnimals.TREATS.getStackFromEnum(Treat.COW));
        BARN.addPurchasable(30, HFAnimals.TREATS.getStackFromEnum(Treat.SHEEP));
        BARN.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        BARN.addPurchasable(2000, HFAnimals.TOOLS.getStackFromEnum(MILKER));
        BARN.addPurchasable(1800, new ItemStack(Items.SHEARS));

        BARN.addOpening(MONDAY, 10000, 15000).addOpening(TUESDAY, 10000, 15000).addOpening(WEDNESDAY, 10000, 15000);
        BARN.addOpening(THURSDAY, 10000, 15000).addOpening(FRIDAY, 10000, 15000).addOpening(SATURDAY, 10000, 15000);
    }

    private static void registerBloodmage() {
        BLOODMAGE.addPurchasable(-30, new ItemStack(Items.SLIME_BALL));
        BLOODMAGE.addPurchasable(-15, new ItemStack(Items.BONE));
        BLOODMAGE.addPurchasable(-80, new ItemStack(Items.ENDER_PEARL));
        BLOODMAGE.addPurchasable(-300, new ItemStack(Items.GOLDEN_APPLE));
        BLOODMAGE.addPurchasable(-5, new ItemStack(Items.ROTTEN_FLESH));
        BLOODMAGE.addPurchasable(-10, new ItemStack(Items.SPIDER_EYE));
        BLOODMAGE.addPurchasable(-30, new ItemStack(Items.POISONOUS_POTATO));
        BLOODMAGE.addPurchasable(-60, new ItemStack(Items.GHAST_TEAR));
        BLOODMAGE.addPurchasable(-20, new ItemStack(Items.FERMENTED_SPIDER_EYE));
        BLOODMAGE.addPurchasable(-70, new ItemStack(Items.BLAZE_ROD));
        BLOODMAGE.addPurchasable(-35, new ItemStack(Items.MAGMA_CREAM));
        BLOODMAGE.addPurchasable(-30, new ItemStack(Items.SPECKLED_MELON));
        BLOODMAGE.addPurchasable(-150, new ItemStack(Items.GOLDEN_CARROT));
        BLOODMAGE.addPurchasable(-100, new ItemStack(Items.RABBIT_FOOT));
        BLOODMAGE.addPurchasable(-40, new ItemStack(Items.GUNPOWDER));
        BLOODMAGE.addPurchasable(-5, new ItemStack(Items.REDSTONE));
        BLOODMAGE.addPurchasable(-10, new ItemStack(Items.GLOWSTONE_DUST));
        BLOODMAGE.addOpening(WEDNESDAY, 19000, 24000).addOpening(WEDNESDAY, 0, 5000).addOpening(SATURDAY, 18000, 24000).addOpening(SATURDAY, 0, 3500);
    }

    private static void registerCafe() {
        CAFE.addPurchasable(0, new ItemStack(Items.POTIONITEM));
        CAFE.addPurchasable(new PurchasableMeal(200, new ResourceLocation(MODID, "salad")));
        CAFE.addPurchasable(new PurchasableMeal(100, new ResourceLocation(MODID, "cookies")));
        CAFE.addPurchasable(new PurchasableMeal(250, new ResourceLocation(MODID, "juice_pineapple")));
        CAFE.addPurchasable(new PurchasableMeal(250, new ResourceLocation(MODID, "corn_baked")));
        CAFE.addPurchasable(new PurchasableMeal(300, new ResourceLocation(MODID, "ice_cream")));

        //Allow the purchasing of cookware at the weekends
        CAFE.addPurchasable(new PurchasableWeekend(25, new ItemStack(COOKBOOK)));
        CAFE.addPurchasable(new PurchasableWeekend(50, UTENSILS.getStackFromEnum(KNIFE)));
        CAFE.addPurchasable(new PurchasableWeekend(250, COOKWARE.getStackFromEnum(COUNTER)));
        CAFE.addPurchasable(new PurchasableWeekend(3000, COOKWARE.getStackFromEnum(FRIDGE)));
        CAFE.addPurchasable(new PurchasableWeekend(2500, COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addPurchasable(new PurchasableWeekend(1500, COOKWARE.getStackFromEnum(FRYING_PAN), COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addPurchasable(new PurchasableWeekend(1000, COOKWARE.getStackFromEnum(POT), COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addPurchasable(new PurchasableWeekend(1200, COOKWARE.getStackFromEnum(MIXER), COOKWARE.getStackFromEnum(COUNTER)));

        //Add recipes for purchase
        CAFE.addPurchasable(new PurchasableRecipe(SPRING, MONDAY, new ResourceLocation(MODID, "juice_vegetable")));
        CAFE.addPurchasable(new PurchasableRecipe(SPRING, TUESDAY, new ResourceLocation(MODID, "sushi")));
        CAFE.addPurchasable(new PurchasableRecipe(SPRING, WEDNESDAY, new ResourceLocation(MODID, "sashimi")));
        CAFE.addPurchasable(new PurchasableRecipe(SPRING, THURSDAY, new ResourceLocation(MODID, "sashimi_chirashi")));
        CAFE.addPurchasable(new PurchasableRecipe(SPRING, FRIDAY, new ResourceLocation(MODID, "cucumber_pickled")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, SATURDAY, new ResourceLocation(MODID, "juice_tomato")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, SUNDAY, new ResourceLocation(MODID, "cornflakes")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, MONDAY, new ResourceLocation(MODID, "ketchup")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, TUESDAY, new ResourceLocation(MODID, "stew_pumpkin")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, THURSDAY, new ResourceLocation(MODID, "doria")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, TUESDAY, new ResourceLocation(MODID, "eggplant_happy")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, WEDNESDAY, new ResourceLocation(MODID, "sandwich")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, SATURDAY, new ResourceLocation(MODID, "spinach_boiled")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, SUNDAY, new ResourceLocation(MODID, "riceballs_toasted")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, MONDAY, new ResourceLocation(MODID, "omelet")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, TUESDAY, new ResourceLocation(MODID, "egg_boiled")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, WEDNESDAY, new ResourceLocation(MODID, "egg_overrice")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, FRIDAY, new ResourceLocation(MODID, "pancake")));

        CAFE.addOpening(MONDAY, 9500, 17000).addOpening(TUESDAY, 9500, 17000).addOpening(WEDNESDAY, 9500, 17000).addOpening(THURSDAY, 9500, 17000);
        CAFE.addOpening(FRIDAY, 9500, 17000).addOpening(SATURDAY, 9500, 17000).addOpening(SUNDAY, 9500, 17000);
    }

    private static void registerCafeKitchen() {
        KITCHEN.addPurchasable(-50, new ItemStack(Blocks.BROWN_MUSHROOM));
        KITCHEN.addPurchasable(-60, new ItemStack(Items.PORKCHOP));
        KITCHEN.addPurchasable(-80, new ItemStack(Items.COOKED_PORKCHOP));
        KITCHEN.addPurchasable(-400, new ItemStack(Items.CAKE));
        KITCHEN.addPurchasable(-250, new ItemStack(Items.COOKIE));
        KITCHEN.addPurchasable(-60, new ItemStack(Items.BEEF));
        KITCHEN.addPurchasable(-80, new ItemStack(Items.COOKED_BEEF));
        KITCHEN.addPurchasable(-40, new ItemStack(Items.CHICKEN));
        KITCHEN.addPurchasable(-50, new ItemStack(Items.COOKED_CHICKEN));
        KITCHEN.addPurchasable(-90, new ItemStack(Items.BAKED_POTATO));
        KITCHEN.addPurchasable(-300, new ItemStack(Items.PUMPKIN_PIE));
        KITCHEN.addPurchasable(-40, new ItemStack(Items.RABBIT));
        KITCHEN.addPurchasable(-50, new ItemStack(Items.COOKED_RABBIT));
        KITCHEN.addPurchasable(-250, new ItemStack(Items.RABBIT_FOOT));
        KITCHEN.addPurchasable(-80, new ItemStack(Items.MUSHROOM_STEW));
        KITCHEN.addPurchasable(-80, new ItemStack(Items.MUTTON));
        KITCHEN.addPurchasable(-100, new ItemStack(Items.COOKED_MUTTON));
        KITCHEN.addPurchasable(-500, new ItemStack(Items.BEETROOT_SOUP));
        KITCHEN.addPurchasable(-10, new ItemStack(Items.SUGAR));
        KITCHEN.addPurchasable(-25, new ItemStack(Items.CHORUS_FRUIT));
        KITCHEN.addOpening(FRIDAY, 6000, 9500).addOpening(FRIDAY, 17000, 20000);
        HFNPCs.CAFE_GRANNY.setHasInfo(null, null); //Remove the opening times from the granny, it's a bonus
    }

    private static void registerCarpenter() {
        CARPENTER.addPurchasable(new PurchasableBuilding(5000L, HFBuildings.SUPERMARKET, Logs.of(96)));
        CARPENTER.addPurchasable(new PurchasableBuilding(4000L, HFBuildings.BARN, Logs.of(96)));
        CARPENTER.addPurchasable(new PurchasableBuilding(3000L, HFBuildings.POULTRY_FARM, Logs.of(96)));
        CARPENTER.addPurchasable(new PurchasableBuilding(1000L, HFBuildings.FESTIVALS, Logs.of(8), Stone.of(4)));
        CARPENTER.addPurchasable(new PurchasableBuilding(3000L, HFBuildings.MINING_HILL, Logs.of(16), Iron.of(8)));
        CARPENTER.addPurchasable(new PurchasableBuilding(9000L, HFBuildings.BLACKSMITH, Logs.of(32), Stone.of(248), Iron.of(16)));
        CARPENTER.addPurchasable(new PurchasableBuilding(12000L, HFBuildings.FISHING_HUT, Logs.of(96), Glass.of(16), Iron.of(16)));
        CARPENTER.addPurchasable(new PurchasableBuilding(3000L, HFBuildings.FISHING_HOLE, Logs.of(16)));
        CARPENTER.addPurchasable(new PurchasableBuilding(10000L, HFBuildings.CAFE, Logs.of(200), Stone.of(48), Glass.of(32), Bricks.of(32), Iron.of(8)));
        CARPENTER.addPurchasable(new PurchasableBuilding(15000L, HFBuildings.CLOCKMAKER, Logs.of(128), Stone.of(64), Iron.of(8)));
        CARPENTER.addPurchasable(new PurchasableBuilding(7000L, HFBuildings.GODDESS_POND, Logs.of(32), Stone.of(64)));
        CARPENTER.addPurchasable(new PurchasableBuilding(25000L, HFBuildings.CHURCH, Logs.of(160), Stone.of(128), Glass.of(8), Iron.of(8)));
        CARPENTER.addPurchasable(new PurchasableBuilding(50000L, HFBuildings.TOWNHALL, Logs.of(640), Stone.of(256), Glass.of(48), Bricks.of(32)));
        CARPENTER.addPurchasable(new PurchasableMaterials(0L, HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING), Logs.of(8)).addTooltip("storage.shipping"));
        CARPENTER.addPurchasable(new PurchasableMaterials(3000L, HFCrops.SPRINKLER.getStackFromEnum(Sprinkler.OLD), Logs.of(4), Copper.of(8)) {
            @Override
            public boolean canList(World world, EntityPlayer player) {
                CalendarDate date = HFApi.calendar.getDate(world);
                return (date.getYear() >= 1 || date.getSeason().ordinal() >= 1) && TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.MINING_HILL);
            }
        }.addTooltip("sprinkler.old"));

        CARPENTER.addPurchasable(new PurchasableMaterials(10000L, HFCrops.SPRINKLER.getStackFromEnum(Sprinkler.IRON), Logs.of(4), Silver.of(8)) {
            @Override
            public boolean canList(World world, EntityPlayer player) {
                return HFApi.quests.hasCompleted(Quests.SPRINKLER, player) && TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.MINING_HILL);
            }
        }.addTooltip("sprinkler.iron.tooltip"));

        CARPENTER.addPurchasable(new PurchasableMaterials(10, new ItemStack(Blocks.LOG)));
        CARPENTER.addPurchasable(new PurchasableMaterials(20, new ItemStack(Blocks.STONE)));
        if (!HFAnimals.CAN_SPAWN) {
            CARPENTER.addPurchasable(new PurchasableMaterials(500, new ItemStack(Items.BED), Logs.of(3)));
        }

        //Selling things to the carpenter
        CARPENTER.addPurchasable(-1, new ItemStack(Blocks.LOG));
        CARPENTER.addPurchasable(-1, new ItemStack(Blocks.STONE));

        CARPENTER.addOpening(MONDAY, 9000, 17500).addOpening(TUESDAY, 9000, 17500).addOpening(WEDNESDAY, 9000, 17500);
        CARPENTER.addOpening(THURSDAY, 9000, 17500).addOpening(FRIDAY, 9000, 17500).addOpening(SUNDAY, 9000, 17500);
    }

    private static void registerMiner() {
        MINER.addPurchasable(1000, HFMining.LADDER.getStackFromEnum(Ladder.DECORATIVE));
        MINER.addPurchasable(250, HFMining.MINING_TOOL.getStackFromEnum(MiningTool.ESCAPE_ROPE));
        //Selling things to the mine
        MINER.addPurchasable(-5, new ItemStack(Items.COAL, 1, 1));
        MINER.addPurchasable(-8, new ItemStack(Items.GOLD_NUGGET));
        MINER.addPurchasable(-10, new ItemStack(Items.COAL));
        MINER.addPurchasable(-15, new ItemStack(Items.QUARTZ));
        MINER.addPurchasable(-45, new ItemStack(Items.IRON_INGOT));
        MINER.addPurchasable(-72, new ItemStack(Items.GOLD_INGOT));

        MINER.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        MINER.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }

    private static void registerPoultry() {
        POULTRY.addPurchasable(50, HFAnimals.TOOLS.getStackFromEnum(CHICKEN_FEED));
        POULTRY.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        POULTRY.addPurchasable(new PurchasableEntity(EntityHarvestChicken.class, 1500, HFAnimals.ANIMAL.getStackFromEnum(CHICKEN), false));
        POULTRY.addPurchasable(250, new ItemStack(Items.NAME_TAG));
        POULTRY.addPurchasable(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        POULTRY.addPurchasable(30, HFAnimals.TREATS.getStackFromEnum(Treat.CHICKEN));
        POULTRY.addPurchasable(500, HFAnimals.TRAY.getStackFromEnum(FEEDER_EMPTY));
        POULTRY.addPurchasable(1000, HFAnimals.TRAY.getStackFromEnum(NEST_EMPTY));
        POULTRY.addPurchasable(7500, HFAnimals.SIZED.getStackFromEnum(INCUBATOR));

        POULTRY.addOpening(MONDAY, 5000, 11000).addOpening(TUESDAY, 5000, 11000).addOpening(WEDNESDAY, 5000, 11000);
        POULTRY.addOpening(THURSDAY, 5000, 11000).addOpening(FRIDAY, 5000, 11000).addOpening(SATURDAY, 5000, 11000);
    }

    private static void registerSupermarket() {
        for (Crop crop : Crop.REGISTRY.getValues()) {
            if (crop != Crop.NULL_CROP) {
                SUPERMARKET.addPurchasable(new PurchasableCropSeeds(crop));
            }
        }

        SUPERMARKET.addPurchasable(250, HFTools.HOE.getStack(ToolTier.BASIC));
        SUPERMARKET.addPurchasable(250, HFTools.SICKLE.getStack(ToolTier.BASIC));
        SUPERMARKET.addPurchasable(500, HFTools.WATERING_CAN.getStack(ToolTier.BASIC));
        SUPERMARKET.addPurchasable(1000, HFTools.AXE.getStack(ToolTier.BASIC));
        SUPERMARKET.addPurchasable(1000, HFTools.HAMMER.getStack(ToolTier.BASIC));

        SUPERMARKET.addPurchasable(new PurchasableBlueFeather(1000, HFNPCs.TOOLS.getStackFromEnum(BLUE_FEATHER)));
        SUPERMARKET.addPurchasable(RICEBALL.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(RICEBALL));
        SUPERMARKET.addPurchasable(100, new ItemStack(Items.BREAD));
        SUPERMARKET.addPurchasable(OIL.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(OIL));
        SUPERMARKET.addPurchasable(FLOUR.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(FLOUR));
        SUPERMARKET.addPurchasable(CURRY_POWDER.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(CURRY_POWDER));
        SUPERMARKET.addPurchasable(DUMPLING_POWDER.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(DUMPLING_POWDER));
        SUPERMARKET.addPurchasable(CHOCOLATE.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(CHOCOLATE));
        SUPERMARKET.addPurchasable(WINE.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(WINE));
        SUPERMARKET.addPurchasable(SALT.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(SALT));

        SUPERMARKET.addOpening(MONDAY, 9000, 17000).addOpening(TUESDAY, 9000, 17000).addOpening(THURSDAY, 9000, 17000);
        SUPERMARKET.addOpening(FRIDAY, 9000, 17000).addOpening(SATURDAY, 11000, 15000);
    }

    private static void registerTackleshop() {
        BAITSHOP.addPurchasable(new Purchasable(Junk.BAIT.getCost(), HFFishing.JUNK.getStackFromEnum(Junk.BAIT)).addTooltip("junk.bait"));
        BAITSHOP.addPurchasable(1000L, HFFishing.FISHING_ROD.getStack(ToolTier.BASIC));
        BAITSHOP.addPurchasable(new Purchasable(1500L, HFFishing.FISHING_BLOCK.getStackFromEnum(FishingBlock.TRAP)).addTooltip("fishing.block.trap"));
        BAITSHOP.addPurchasable(new PurchasableMaterials(3000L, HFFishing.FISHING_BLOCK.getStackFromEnum(FishingBlock.HATCHERY), Logs.of(8), Wool.of(1)) {
            @Override
            public boolean canList(World world, EntityPlayer player) {
                return HFApi.quests.hasCompleted(Quests.HATCHERY, player);
            }
        }.addTooltip("fishing.block.hatchery"));


        //Selling things to the carpenter
        BAITSHOP.addPurchasable(-100, new ItemStack(Items.PRISMARINE_SHARD));
        BAITSHOP.addPurchasable(-150, new ItemStack(Items.PRISMARINE_CRYSTALS));
        BAITSHOP.addPurchasable(-10, new ItemStack(Items.FISH, 1, 0));
        BAITSHOP.addPurchasable(-30, new ItemStack(Items.FISH, 1, 1));
        BAITSHOP.addPurchasable(-50, new ItemStack(Items.FISH, 1, 2));
        BAITSHOP.addPurchasable(-100, new ItemStack(Items.FISH, 1, 3));
        for (Fish fish: Fish.values()) {
            long sell = (fish.getSellValue(fish.getLengthFromSizeOfFish(MEDIUM_FISH))) - fish.getSellValue(fish.getLengthFromSizeOfFish(MEDIUM_FISH)) % 10;
            BAITSHOP.addPurchasable(new PurchasableObtained(sell, HFFishing.FISH.getStackFromEnum(fish)));
        }

        BAITSHOP.addOpening(TUESDAY, 13000, 19000).addOpening(WEDNESDAY, 13000, 19000).addOpening(THURSDAY, 13000, 19000).addOpening(FRIDAY, 13000, 19000);
    }

    private static void registerTrader() {
        TRADER.addPurchasable(-50, new ItemStack(Items.MILK_BUCKET));
        TRADER.addPurchasable(-10, new ItemStack(Items.EGG));
        TRADER.addPurchasable(-30, new ItemStack(Items.LEATHER));
        TRADER.addPurchasable(-20, new ItemStack(Items.FEATHER));
        TRADER.addPurchasable(-20, new ItemStack(Items.RABBIT_HIDE));
        TRADER.addPurchasable(-5, new ItemStack(Items.STRING));
        for (int i = 0; i < 16; i++) {
            TRADER.addPurchasable(-15, new ItemStack(Blocks.WOOL, 1, 0));
        }

        TRADER.addOpening(MONDAY, 6000, 10000).addOpening(TUESDAY, 6000, 10000).addOpening(WEDNESDAY, 6000, 10000).addOpening(THURSDAY, 6000, 10000);
        TRADER.addOpening(FRIDAY, 6000, 10000).addOpening(SATURDAY, 6000, 10000).addOpening(SUNDAY, 6000, 10000);
        HFNPCs.TRADER.setHasInfo(null, null); //Remove the opening times
    }

    public static boolean TWENTY_FOUR_HOUR_SHOPPING;

    public static void configure() {
        TWENTY_FOUR_HOUR_SHOPPING = getBoolean("Shops are open all the time", false);
    }
}