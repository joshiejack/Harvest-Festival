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
    public static final Shop BARN = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "barn"), HFNPCs.BARN_OWNER);
    public static final Shop CAFE = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "cafe"), HFNPCs.CAFE_OWNER);
    public static final Shop CARPENTER = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "carpenter"), HFNPCs.BUILDER);
    public static final Shop POULTRY = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "poultry"), HFNPCs.POULTRY);
    public static final Shop SUPERMARKET = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "general"), HFNPCs.GS_OWNER);
    public static final Shop MINER = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "miner"), HFNPCs.MINER);
    //Added in 0.6+
    public static final Shop BAITSHOP = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "baitshop"), HFNPCs.FISHERMAN);
    public static final Shop BLOODMAGE = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "bloodmage"), HFNPCs.CLOCKMAKER).setSpecialPurchaseRules(new SpecialRulesFriendship(HFNPCs.CLOCKMAKER, 15000));
    public static final Shop KITCHEN = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "kitchen"), HFNPCs.CAFE_GRANNY).setSpecialPurchaseRules(new SpecialRulesFriendship(HFNPCs.CAFE_GRANNY, 15000));
    public static final Shop TRADER = ShopRegistry.INSTANCE.newShop(new ResourceLocation(MODID, "trader"), HFNPCs.TRADER).setSpecialPurchaseRules(new SpecialRulesFriendship(HFNPCs.TRADER, 15000));

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
        BARN.addPurchasable(3000, HFAnimals.TOOLS.getStackFromEnum(MIRACLE_POTION), 1);
        BARN.addPurchasable(500, HFAnimals.TROUGH.getStackFromEnum(WOOD), 3);
        BARN.addPurchasable(250, new ItemStack(Items.NAME_TAG));
        BARN.addPurchasable(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        BARN.addPurchasable(30, HFAnimals.TREATS.getStackFromEnum(Treat.COW));
        BARN.addPurchasable(30, HFAnimals.TREATS.getStackFromEnum(Treat.SHEEP));
        BARN.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(BRUSH), 1);
        BARN.addPurchasable(2000, HFAnimals.TOOLS.getStackFromEnum(MILKER), 1);
        BARN.addPurchasable(1800, new ItemStack(Items.SHEARS), 1);

        BARN.addOpening(MONDAY, 10000, 15000).addOpening(TUESDAY, 10000, 15000).addOpening(WEDNESDAY, 10000, 15000);
        BARN.addOpening(THURSDAY, 10000, 15000).addOpening(FRIDAY, 10000, 15000).addOpening(SATURDAY, 10000, 15000);
    }

    private static void registerBloodmage() {
        BLOODMAGE.addPurchasable(-100, new ItemStack(Items.SLIME_BALL), 3);
        BLOODMAGE.addPurchasable(-150, new ItemStack(Items.BONE), 2);
        BLOODMAGE.addPurchasable(-800, new ItemStack(Items.ENDER_PEARL), 4);
        BLOODMAGE.addPurchasable(-300, new ItemStack(Items.GOLDEN_APPLE), 3);
        BLOODMAGE.addPurchasable(-50, new ItemStack(Items.ROTTEN_FLESH), 5);
        BLOODMAGE.addPurchasable(-100, new ItemStack(Items.SPIDER_EYE), 4);
        BLOODMAGE.addPurchasable(-30, new ItemStack(Items.POISONOUS_POTATO), 5);
        BLOODMAGE.addPurchasable(-600, new ItemStack(Items.GHAST_TEAR), 6);
        BLOODMAGE.addPurchasable(-150, new ItemStack(Items.FERMENTED_SPIDER_EYE), 5);
        BLOODMAGE.addPurchasable(-700, new ItemStack(Items.BLAZE_ROD), 7);
        BLOODMAGE.addPurchasable(-350, new ItemStack(Items.MAGMA_CREAM), 7);
        BLOODMAGE.addPurchasable(-50, new ItemStack(Items.SPECKLED_MELON), 5);
        BLOODMAGE.addPurchasable(-150, new ItemStack(Items.GOLDEN_CARROT), 5);
        BLOODMAGE.addPurchasable(-100, new ItemStack(Items.RABBIT_FOOT), 2);
        BLOODMAGE.addPurchasable(-200, new ItemStack(Items.GUNPOWDER), 4);
        BLOODMAGE.addPurchasable(-30, new ItemStack(Items.REDSTONE));
        BLOODMAGE.addPurchasable(-50, new ItemStack(Items.GLOWSTONE_DUST), 5);
        BLOODMAGE.addOpening(WEDNESDAY, 19000, 24000).addOpening(WEDNESDAY, 0, 5000).addOpening(SATURDAY, 18000, 24000).addOpening(SATURDAY, 0, 3500);
    }

    private static void registerCafe() {
        CAFE.addPurchasable(0, new ItemStack(Items.POTIONITEM));
        CAFE.addPurchasable(new PurchasableMeal(200, new ResourceLocation(MODID, "salad")).setStock(10));
        CAFE.addPurchasable(new PurchasableMeal(100, new ResourceLocation(MODID, "cookies")).setStock(3));
        CAFE.addPurchasable(new PurchasableMeal(250, new ResourceLocation(MODID, "juice_pineapple")).setStock(5));
        CAFE.addPurchasable(new PurchasableMeal(250, new ResourceLocation(MODID, "corn_baked")).setStock(5));
        CAFE.addPurchasable(new PurchasableMeal(300, new ResourceLocation(MODID, "ice_cream")).setStock(10));

        //Allow the purchasing of cookware at the weekends
        CAFE.addPurchasable(new PurchasableWeekend(25, new ItemStack(COOKBOOK)).setStock(1));
        CAFE.addPurchasable(new PurchasableWeekend(50, UTENSILS.getStackFromEnum(KNIFE)).setStock(1));
        CAFE.addPurchasable(new PurchasableWeekend(250, COOKWARE.getStackFromEnum(COUNTER)).setStock(5));
        CAFE.addPurchasable(new PurchasableWeekend(3000, COOKWARE.getStackFromEnum(FRIDGE)).setStock(1));
        CAFE.addPurchasable(new PurchasableWeekend(2500, COOKWARE.getStackFromEnum(OVEN_OFF)).setStock(1));
        CAFE.addPurchasable(new PurchasableWeekend(1500, COOKWARE.getStackFromEnum(FRYING_PAN), COOKWARE.getStackFromEnum(OVEN_OFF)).setStock(1));
        CAFE.addPurchasable(new PurchasableWeekend(1000, COOKWARE.getStackFromEnum(POT), COOKWARE.getStackFromEnum(OVEN_OFF)).setStock(1));
        CAFE.addPurchasable(new PurchasableWeekend(1200, COOKWARE.getStackFromEnum(MIXER), COOKWARE.getStackFromEnum(COUNTER)).setStock(1));

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
        KITCHEN.addPurchasable(-50, new ItemStack(Blocks.BROWN_MUSHROOM), 5);
        KITCHEN.addPurchasable(-60, new ItemStack(Items.PORKCHOP), 3);
        KITCHEN.addPurchasable(-80, new ItemStack(Items.COOKED_PORKCHOP), 4);
        KITCHEN.addPurchasable(-400, new ItemStack(Items.CAKE), 4);
        KITCHEN.addPurchasable(-250, new ItemStack(Items.COOKIE), 5);
        KITCHEN.addPurchasable(-60, new ItemStack(Items.BEEF), 3);
        KITCHEN.addPurchasable(-80, new ItemStack(Items.COOKED_BEEF), 4);
        KITCHEN.addPurchasable(-40, new ItemStack(Items.CHICKEN), 5);
        KITCHEN.addPurchasable(-50, new ItemStack(Items.COOKED_CHICKEN), 5);
        KITCHEN.addPurchasable(-90, new ItemStack(Items.BAKED_POTATO), 5);
        KITCHEN.addPurchasable(-300, new ItemStack(Items.PUMPKIN_PIE), 2);
        KITCHEN.addPurchasable(-40, new ItemStack(Items.RABBIT), 4);
        KITCHEN.addPurchasable(-50, new ItemStack(Items.COOKED_RABBIT), 5);
        KITCHEN.addPurchasable(-250, new ItemStack(Items.RABBIT_FOOT), 2);
        KITCHEN.addPurchasable(-80, new ItemStack(Items.MUSHROOM_STEW), 8);
        KITCHEN.addPurchasable(-80, new ItemStack(Items.MUTTON), 8);
        KITCHEN.addPurchasable(-100, new ItemStack(Items.COOKED_MUTTON), 4);
        KITCHEN.addPurchasable(-500, new ItemStack(Items.BEETROOT_SOUP), 2);
        KITCHEN.addPurchasable(-10, new ItemStack(Items.SUGAR), 20);
        KITCHEN.addPurchasable(-25, new ItemStack(Items.CHORUS_FRUIT), 15);
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
        }.setStock(10).addTooltip("sprinkler.old"));

        CARPENTER.addPurchasable(new PurchasableMaterials(10000L, HFCrops.SPRINKLER.getStackFromEnum(Sprinkler.IRON), Logs.of(4), Silver.of(8)) {
            @Override
            public boolean canList(World world, EntityPlayer player) {
                return HFApi.quests.hasCompleted(Quests.SPRINKLER, player) && TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.MINING_HILL);
            }
        }.setStock(3).addTooltip("sprinkler.iron.tooltip"));

        CARPENTER.addPurchasable(new PurchasableMaterials(10, new ItemStack(Blocks.LOG)));
        CARPENTER.addPurchasable(new PurchasableMaterials(20, new ItemStack(Blocks.STONE)));
        if (!HFAnimals.CAN_SPAWN) {
            CARPENTER.addPurchasable(new PurchasableMaterials(500, new ItemStack(Items.BED), Logs.of(3)).setStock(1));
        }

        //Selling things to the carpenter
        CARPENTER.addPurchasable(-1, new ItemStack(Blocks.LOG));
        CARPENTER.addPurchasable(-1, new ItemStack(Blocks.STONE));

        CARPENTER.addOpening(MONDAY, 9000, 17500).addOpening(TUESDAY, 9000, 17500).addOpening(WEDNESDAY, 9000, 17500);
        CARPENTER.addOpening(THURSDAY, 9000, 17500).addOpening(FRIDAY, 9000, 17500).addOpening(SUNDAY, 9000, 17500);
    }

    private static void registerMiner() {
        MINER.addPurchasable(1000, HFMining.LADDER.getStackFromEnum(Ladder.DECORATIVE), 3);
        MINER.addPurchasable(250, HFMining.MINING_TOOL.getStackFromEnum(MiningTool.ESCAPE_ROPE), 10);
        //Selling things to the mine
        MINER.addPurchasable(-30, new ItemStack(Items.COAL, 1, 1));
        MINER.addPurchasable(-15, new ItemStack(Items.GOLD_NUGGET));
        MINER.addPurchasable(-50, new ItemStack(Items.COAL), 8);
        MINER.addPurchasable(-60, new ItemStack(Items.QUARTZ), 8);
        MINER.addPurchasable(-100, new ItemStack(Items.IRON_INGOT), 5);
        MINER.addPurchasable(-140, new ItemStack(Items.GOLD_INGOT), 2);

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
        POULTRY.addPurchasable(500, HFAnimals.TRAY.getStackFromEnum(FEEDER_EMPTY), 3);
        POULTRY.addPurchasable(1000, HFAnimals.TRAY.getStackFromEnum(NEST_EMPTY), 3);
        POULTRY.addPurchasable(7500, HFAnimals.SIZED.getStackFromEnum(INCUBATOR), 1);

        POULTRY.addOpening(MONDAY, 5000, 11000).addOpening(TUESDAY, 5000, 11000).addOpening(WEDNESDAY, 5000, 11000);
        POULTRY.addOpening(THURSDAY, 5000, 11000).addOpening(FRIDAY, 5000, 11000).addOpening(SATURDAY, 5000, 11000);
    }

    private static void registerSupermarket() {
        for (Crop crop : Crop.REGISTRY.getValues()) {
            if (crop != Crop.NULL_CROP) {
                SUPERMARKET.addPurchasable(new PurchasableCropSeeds(crop));
            }
        }

        SUPERMARKET.addPurchasable(250, HFTools.HOE.getStack(ToolTier.BASIC), 1);
        SUPERMARKET.addPurchasable(250, HFTools.SICKLE.getStack(ToolTier.BASIC), 1);
        SUPERMARKET.addPurchasable(500, HFTools.WATERING_CAN.getStack(ToolTier.BASIC), 1);
        SUPERMARKET.addPurchasable(1000, HFTools.AXE.getStack(ToolTier.BASIC), 1);
        SUPERMARKET.addPurchasable(1000, HFTools.HAMMER.getStack(ToolTier.BASIC), 1);

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
        BAITSHOP.addPurchasable(1000L, HFFishing.FISHING_ROD.getStack(ToolTier.BASIC), 1);
        BAITSHOP.addPurchasable(new Purchasable(1500L, HFFishing.FISHING_BLOCK.getStackFromEnum(FishingBlock.TRAP)).setStock(10).addTooltip("fishing.block.trap"));
        BAITSHOP.addPurchasable(new PurchasableMaterials(3000L, HFFishing.FISHING_BLOCK.getStackFromEnum(FishingBlock.HATCHERY), Logs.of(8), Wool.of(1)) {
            @Override
            public boolean canList(World world, EntityPlayer player) {
                return HFApi.quests.hasCompleted(Quests.HATCHERY, player);
            }
        }.setStock(1).addTooltip("fishing.block.hatchery"));


        //Selling things to the carpenter
        BAITSHOP.addPurchasable(-100, new ItemStack(Items.PRISMARINE_SHARD), 5);
        BAITSHOP.addPurchasable(-150, new ItemStack(Items.PRISMARINE_CRYSTALS), 3);
        BAITSHOP.addPurchasable(-10, new ItemStack(Items.FISH, 1, 0), 20);
        BAITSHOP.addPurchasable(-30, new ItemStack(Items.FISH, 1, 1), 15);
        BAITSHOP.addPurchasable(-50, new ItemStack(Items.FISH, 1, 2));
        BAITSHOP.addPurchasable(-100, new ItemStack(Items.FISH, 1, 3), 5);
        for (Fish fish: Fish.values()) {
            long sell = (fish.getSellValue(fish.getLengthFromSizeOfFish(MEDIUM_FISH))) - fish.getSellValue(fish.getLengthFromSizeOfFish(MEDIUM_FISH)) % 10;
            BAITSHOP.addPurchasable(new PurchasableObtained(sell, HFFishing.FISH.getStackFromEnum(fish)).setStock(100));
        }

        BAITSHOP.addOpening(TUESDAY, 13000, 19000).addOpening(WEDNESDAY, 13000, 19000).addOpening(THURSDAY, 13000, 19000).addOpening(FRIDAY, 13000, 19000);
    }

    private static void registerTrader() {
        TRADER.addPurchasable(-60, new ItemStack(Items.MILK_BUCKET), 3);
        TRADER.addPurchasable(-30, new ItemStack(Items.EGG));
        TRADER.addPurchasable(-120, new ItemStack(Items.LEATHER), 3);
        TRADER.addPurchasable(-50, new ItemStack(Items.FEATHER), 8);
        TRADER.addPurchasable(-80, new ItemStack(Items.RABBIT_HIDE), 5);
        TRADER.addPurchasable(-20, new ItemStack(Items.STRING));
        for (int i = 0; i < 16; i++) {
            TRADER.addPurchasable(-150, new ItemStack(Blocks.WOOL, 1, 0), 3);
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