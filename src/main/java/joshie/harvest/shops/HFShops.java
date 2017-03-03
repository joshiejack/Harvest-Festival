package joshie.harvest.shops;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.block.BlockTrough.Trough;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockSprinkler.Sprinkler;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.block.BlockAquatic.Aquatic;
import joshie.harvest.fishing.block.BlockFloating.Floating;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.gathering.block.BlockNature.NaturalBlock;
import joshie.harvest.knowledge.HFKnowledge;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.knowledge.item.ItemBook.Book;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockElevator.Elevator;
import joshie.harvest.mining.block.BlockLadder.Ladder;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.mining.item.ItemMiningTool.MiningTool;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.npc.NPCHolidayStore;
import joshie.harvest.npcs.npc.NPCHolidayStoreSpecial;
import joshie.harvest.quests.HFQuests;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestRecipe;
import joshie.harvest.quests.block.BlockQuestBoard.QuestBlock;
import joshie.harvest.shops.purchasable.*;
import joshie.harvest.shops.requirement.*;
import joshie.harvest.shops.requirement.String;
import joshie.harvest.shops.rules.SpecialRulesFriendship;
import joshie.harvest.shops.rules.SpecialRulesQuest;
import joshie.harvest.tools.HFTools;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

@HFLoader
public class HFShops {
    public static final Shop BARN = newShop(new ResourceLocation(MODID, "barn"), HFNPCs.BARN_OWNER);
    public static final Shop CAFE = newShop(new ResourceLocation(MODID, "cafe"), HFNPCs.CAFE_OWNER);
    public static final Shop CARPENTER = newShop(new ResourceLocation(MODID, "carpenter"), HFNPCs.CARPENTER);
    public static final Shop POULTRY = newShop(new ResourceLocation(MODID, "poultry"), HFNPCs.POULTRY);
    public static final Shop SUPERMARKET = newShop(new ResourceLocation(MODID, "general"), HFNPCs.GS_OWNER);
    public static final Shop MINER = newShop(new ResourceLocation(MODID, "miner"), HFNPCs.MINER);
    //Added in 0.6+
    public static final Shop BAITSHOP = newShop(new ResourceLocation(MODID, "baitshop"), HFNPCs.FISHERMAN);
    public static final Shop CLOCKMAKER = newShop(new ResourceLocation(MODID, "clockmaker"), HFNPCs.CLOCKMAKER);
    public static final Shop BLOODMAGE = newShop(new ResourceLocation(MODID, "bloodmage"), null).setSpecialSellingRules(new SpecialRulesFriendship(HFNPCs.CLOCKMAKER, 15000)).setOpensOnHolidays();
    public static final Shop KITCHEN = newShop(new ResourceLocation(MODID, "kitchen"), HFNPCs.CAFE_GRANNY).setSpecialSellingRules(new SpecialRulesFriendship(HFNPCs.CAFE_GRANNY, 15000));
    public static final Shop TRADER = newShop(new ResourceLocation(MODID, "trader"), HFNPCs.TRADER).setSpecialSellingRules(new SpecialRulesFriendship(HFNPCs.TRADER, 15000));
    public static final Shop COOKING_FESTIVAL_FOOD = newHolidayShop(new ResourceLocation(MODID, "cooking"), HFNPCs.CAFE_GRANNY, HFFestivals.COOKING_CONTEST);
    public static final Shop COOKING_FESTIVAL_RECIPES = newHolidayShop(new ResourceLocation(MODID, "recipes"), HFNPCs.CAFE_OWNER, HFFestivals.COOKING_CONTEST);
    public static final Shop COW_FESTIVAL_BARGAINS = newHolidayShop(new ResourceLocation(MODID, "cow"), HFNPCs.BARN_OWNER, HFFestivals.COW_FESTIVAL);
    public static final Shop COW_FESTIVAL_DAIRY_QUEEN = newHolidayShop(new ResourceLocation(MODID, "dairy"), HFNPCs.CAFE_OWNER, HFFestivals.COW_FESTIVAL);
    public static final Shop SHEEP_FESTIVAL_SALE = newHolidayShop(new ResourceLocation(MODID, "sheep"), HFNPCs.MILKMAID, HFFestivals.SHEEP_FESTIVAL);
    public static final Shop SHEEP_FESTIVAL_KNITTNG = newHolidayShop(new ResourceLocation(MODID, "knitting"), HFNPCs.CAFE_GRANNY, HFFestivals.SHEEP_FESTIVAL);
    public static final Shop CHICKEN_FESTIVAL_MFC = newHolidayShop(new ResourceLocation(MODID, "mfc"), HFNPCs.CAFE_OWNER, HFFestivals.CHICKEN_FESTIVAL);

    @SuppressWarnings("unused")
    public static void postInit() {
        registerBarn();
        registerBloodmage();
        registerCafe();
        registerCafeKitchen();
        registerCarpenter();
        registerClockmaker();
        registerMiner();
        registerPoultry();
        registerSupermarket();
        registerTackleshop();
        registerTrader();

        //Event specific
        registerCooking();
        registerRecipes();
        registerCowBargains();
        registerDairyQueen();
        registerSaleSheep();
        registerGrannysKnits();
        registerMinecraftFriedChicken();
    }
    
    private static void registerBarn() {
        BARN.addPurchasable(new Purchasable(100, HFCrops.GRASS.getCropStack(1)).addTooltip("crop.grass.item"));
        BARN.addPurchasable(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        BARN.addPurchasable(30, HFAnimals.TREATS.getStackFromEnum(Treat.COW));
        BARN.addPurchasable(30, HFAnimals.TREATS.getStackFromEnum(Treat.SHEEP));
        BARN.addPurchasable(new PurchasableEntity(EntityHarvestCow.class, 5000, HFAnimals.ANIMAL.getStackFromEnum(COW)).setNote(HFNotes.COW_CARE));
        BARN.addPurchasable(new PurchasableEntity(EntityHarvestSheep.class, 4000, HFAnimals.ANIMAL.getStackFromEnum(SHEEP)).setNote(HFNotes.SHEEP_CARE));
        BARN.addPurchasable(500, HFAnimals.TROUGH.getStackFromEnum(WOOD), 3);
        BARN.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        BARN.addPurchasable(3000, HFAnimals.TOOLS.getStackFromEnum(MIRACLE_POTION), 1);
        BARN.addPurchasable(250, new ItemStack(Items.NAME_TAG));
        BARN.addPurchasable(150, new ItemStack(Items.LEAD));
        BARN.addPurchasable(1000, new ItemStack(Items.SADDLE));
        BARN.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(BRUSH), 1);
        BARN.addPurchasable(2000, HFAnimals.TOOLS.getStackFromEnum(MILKER), 1);
        BARN.addPurchasable(2000, new ItemStack(Items.SHEARS), 1);
        BARN.setSpecialRules(new SpecialRulesQuest(Quests.JIM_MEET));
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
        BLOODMAGE.addPurchasable(-30, new ItemStack(Items.REDSTONE), 10);
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
        //Add three random meals
        CAFE.addPurchasable(new PurchasableRandomMeal(5));
        CAFE.addPurchasable(new PurchasableRandomMeal(13));
        CAFE.addPurchasable(new PurchasableRandomMeal(17));

        //Allow the purchasing of cookware at the weekends
        CAFE.addPurchasable(new PurchasableWeekend(25, new ItemStack(COOKBOOK)).setStock(1).setNote(HFNotes.RECIPE_BOOK));
        CAFE.addPurchasable(new PurchasableWeekend(50, UTENSILS.getStackFromEnum(KNIFE)).setStock(1).setNote(HFNotes.KITCHEN_COUNTER));
        CAFE.addPurchasable(new PurchasableWeekend(250, COOKWARE.getStackFromEnum(COUNTER)).setStock(5).setNote(HFNotes.KITCHEN_COUNTER));
        CAFE.addPurchasable(new PurchasableWeekend(3000, COOKWARE.getStackFromEnum(FRIDGE)).setStock(1).setNote(HFNotes.FRIDGE));
        CAFE.addPurchasable(new PurchasableWeekend(2500, COOKWARE.getStackFromEnum(OVEN_OFF)).setStock(1).setNote(HFNotes.OVEN));
        CAFE.addPurchasable(new PurchasableWeekend(1500, COOKWARE.getStackFromEnum(FRYING_PAN), COOKWARE.getStackFromEnum(OVEN_OFF)).setStock(1).setNote(HFNotes.POTPAN));
        CAFE.addPurchasable(new PurchasableWeekend(1000, COOKWARE.getStackFromEnum(POT), COOKWARE.getStackFromEnum(OVEN_OFF)).setStock(1).setNote(HFNotes.POTPAN));
        CAFE.addPurchasable(new PurchasableWeekend(1200, COOKWARE.getStackFromEnum(MIXER)).setStock(1).setNote(HFNotes.MIXER));

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
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, WEDNESDAY, new ResourceLocation(MODID, "rice_fried")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, THURSDAY, new ResourceLocation(MODID, "doria")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, FRIDAY, new ResourceLocation(MODID, "juice_fruit")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, SATURDAY, new ResourceLocation(MODID, "salad_herb")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, SUNDAY, new ResourceLocation(MODID, "soup_herb")));
        CAFE.addPurchasable(new PurchasableRecipe(SUMMER, SUNDAY, new ResourceLocation(MODID, "sandwich_herb")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, MONDAY, new ResourceLocation(MODID, "sweet_potatoes")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, TUESDAY, new ResourceLocation(MODID, "eggplant_happy")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, WEDNESDAY, new ResourceLocation(MODID, "sandwich")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, THURSDAY, new ResourceLocation(MODID, "sandwich_fruit")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, FRIDAY, new ResourceLocation(MODID, "latte_fruit")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, SATURDAY, new ResourceLocation(MODID, "spinach_boiled")));
        CAFE.addPurchasable(new PurchasableRecipe(AUTUMN, SUNDAY, new ResourceLocation(MODID, "riceballs_toasted")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, MONDAY, new ResourceLocation(MODID, "omelet")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, TUESDAY, new ResourceLocation(MODID, "egg_boiled")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, WEDNESDAY, new ResourceLocation(MODID, "egg_overrice")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, THURSDAY, new ResourceLocation(MODID, "juice_mix")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, FRIDAY, new ResourceLocation(MODID, "pancake")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, SATURDAY, new ResourceLocation(MODID, "rice_matsutake")));
        CAFE.addPurchasable(new PurchasableRecipe(WINTER, SUNDAY, new ResourceLocation(MODID, "rice_mushroom")));
        CAFE.addPurchasable(new PurchasableRecipeShipped(new ResourceLocation(MODID, "juice_grape"), HFCrops.GRAPE));
        CAFE.addPurchasable(new PurchasableRecipeShipped(new ResourceLocation(MODID, "juice_apple"), HFCrops.APPLE));
        CAFE.addPurchasable(new PurchasableRecipeShipped(new ResourceLocation(MODID, "juice_orange"), HFCrops.ORANGE));
        CAFE.addPurchasable(new PurchasableRecipeShipped(new ResourceLocation(MODID, "juice_banana"), HFCrops.BANANA));
        CAFE.addPurchasable(new PurchasableRecipeShipped(new ResourceLocation(MODID, "juice_peach"), HFCrops.PEACH));
        CAFE.setSpecialRules(new SpecialRulesQuest(Quests.LIARA_MEET));
        CAFE.addOpening(MONDAY, 9500, 17000).addOpening(TUESDAY, 9500, 17000).addOpening(WEDNESDAY, 9500, 17000).addOpening(THURSDAY, 9500, 17000);
        CAFE.addOpening(FRIDAY, 9500, 17000).addOpening(SATURDAY, 9500, 17000).addOpening(SUNDAY, 9500, 17000);
    }

    private static void registerCafeKitchen() {
        KITCHEN.addPurchasable(-350, new ItemStack(Items.BREAD), 2);
        KITCHEN.addPurchasable(-50, new ItemStack(Blocks.BROWN_MUSHROOM), 5);
        KITCHEN.addPurchasable(-60, new ItemStack(Items.PORKCHOP), 3);
        KITCHEN.addPurchasable(-80, new ItemStack(Items.COOKED_PORKCHOP), 4);
        KITCHEN.addPurchasable(-400, new ItemStack(Items.CAKE), 4);
        KITCHEN.addPurchasable(-30, new ItemStack(Items.COOKIE), 5);
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
        HFNPCs.CAFE_GRANNY.setHasInfo(null); //Remove the opening times from the granny, it's a bonus
    }

    private static void registerCarpenter() {
        CARPENTER.addPurchasable(new PurchasableBuilding(5000L, HFBuildings.SUPERMARKET, Logs.of(96), Stone.of(32)));
        CARPENTER.addPurchasable(new PurchasableBuilding(4000L, HFBuildings.BARN, Logs.of(96)));
        CARPENTER.addPurchasable(new PurchasableBuilding(3000L, HFBuildings.POULTRY_FARM, Logs.of(96)));
        CARPENTER.addPurchasable(new PurchasableBuilding(1000L, HFBuildings.FESTIVAL_GROUNDS, Logs.of(8), Stone.of(4)));
        CARPENTER.addPurchasable(new PurchasableBuilding(3000L, HFBuildings.MINING_HILL, Logs.of(16), Iron.of(8)));
        CARPENTER.addPurchasable(new PurchasableBuilding(9000L, HFBuildings.BLACKSMITH, Logs.of(32), Stone.of(248), Iron.of(16)));
        CARPENTER.addPurchasable(new PurchasableBuilding(12000L, HFBuildings.FISHING_HUT, Logs.of(96), Glass.of(16), Iron.of(16)));
        CARPENTER.addPurchasable(new PurchasableBuilding(3000L, HFBuildings.FISHING_HOLE, Logs.of(16)));
        CARPENTER.addPurchasable(new PurchasableBuilding(10000L, HFBuildings.CAFE, Logs.of(200), Stone.of(48), Glass.of(32), Bricks.of(32)));
        CARPENTER.addPurchasable(new PurchasableBuilding(15000L, HFBuildings.CLOCKMAKER, Logs.of(128), Stone.of(64), Iron.of(8)));
        CARPENTER.addPurchasable(new PurchasableBuilding(7000L, HFBuildings.GODDESS_POND, Logs.of(32), Stone.of(64)));
        CARPENTER.addPurchasable(new PurchasableBuilding(25000L, HFBuildings.CHURCH, Logs.of(160), Stone.of(128), Glass.of(8), Iron.of(8)));
        CARPENTER.addPurchasable(new PurchasableBuilding(50000L, HFBuildings.TOWNHALL, Logs.of(640), Stone.of(256), Glass.of(48), Bricks.of(32)));
        CARPENTER.addPurchasable(new PurchasableMaterials(0L, HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING), Logs.of(8)).addTooltip("storage.shipping").setNote(HFNotes.SHIPPING));
        CARPENTER.addPurchasable(new PurchasableMaterials(0L, HFCore.STORAGE.getStackFromEnum(Storage.MAILBOX), Logs.of(4)).addTooltip("storage.mailbox").setNote(HFNotes.MAILBOX));
        CARPENTER.addPurchasable(new PurchasableMaterials(3000L, HFCrops.SPRINKLER.getStackFromEnum(Sprinkler.OLD), Logs.of(4), Copper.of(8)) {
            @Override
            public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
                CalendarDate date = HFApi.calendar.getDate(world);
                return (date.getYear() >= 1 || date.getSeason().ordinal() >= 1);
            }
        }.setStock(10).addTooltip("sprinkler.old"));

        CARPENTER.addPurchasable(new PurchasableMaterials(10000L, HFCrops.SPRINKLER.getStackFromEnum(Sprinkler.IRON), Logs.of(4), Silver.of(8)) {
            @Override
            public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
                return HFApi.quests.hasCompleted(Quests.SELL_SPRINKLER, player);
            }
        }.setStock(3).addTooltip("sprinkler.iron.tooltip"));

        CARPENTER.addPurchasable(new PurchasableMaterials(500L, HFQuests.QUEST_BLOCK.getStackFromEnum(QuestBlock.BOARD), Logs.of(8), Gold.of(4)) {
            @Override
            public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
                return TownHelper.getClosestTownToEntity(player, false).hasBuilding(HFBuildings.TOWNHALL);
            }
        }.setStock(1));

        CARPENTER.addPurchasable(new PurchasableMaterials(10, new ItemStack(Blocks.LOG)));
        CARPENTER.addPurchasable(new PurchasableMaterials(20, new ItemStack(Blocks.STONE)));
        if (!HFAnimals.CAN_SPAWN) {
            CARPENTER.addPurchasable(new PurchasableMaterials(500, new ItemStack(Items.BED), Planks.of(3)).setStock(1));
        }

        //Selling things to the carpenter
        CARPENTER.addPurchasable(-1, new ItemStack(Blocks.LOG), 512);
        CARPENTER.addPurchasable(-1, new ItemStack(Blocks.STONE), 512);
        CARPENTER.setSpecialRules(new SpecialRulesQuest(Quests.YULIF_MEET));
        CARPENTER.addOpening(MONDAY, 9000, 17500).addOpening(TUESDAY, 9000, 17500).addOpening(WEDNESDAY, 9000, 17500);
        CARPENTER.addOpening(THURSDAY, 9000, 17500).addOpening(FRIDAY, 9000, 17500).addOpening(SUNDAY, 9000, 17500);
    }

    private static void registerClockmaker() {
        CLOCKMAKER.addPurchasable(500, new ItemStack(Items.CLOCK));
        CLOCKMAKER.addPurchasable(150, new ItemStack(Items.COMPASS));
        CLOCKMAKER.addPurchasable(100, new ItemStack(Items.MAP));
        CLOCKMAKER.addPurchasable(750, HFKnowledge.BOOK.getStackFromEnum(Book.CALENDAR));
        CLOCKMAKER.addOpening(MONDAY, 8000, 15000).addOpening(TUESDAY, 8000, 15000).addOpening(WEDNESDAY, 8000, 15000)
                .addOpening(THURSDAY, 8000, 15000).addOpening(FRIDAY, 8000, 15000);
    }

    private static void registerMiner() {
        MINER.addPurchasable(40, new ItemStack(Blocks.TORCH), 160);
        MINER.addPurchasable(250, HFMining.MINING_TOOL.getStackFromEnum(MiningTool.ESCAPE_ROPE), 10);
        MINER.addPurchasable(1000, HFMining.LADDER.getStackFromEnum(Ladder.DECORATIVE), 3);
        MINER.addPurchasable(new PurchasableObtainedMaterial(200, HFMining.MINING_TOOL.getStackFromEnum(MiningTool.ELEVATOR_CABLE, 8), Copper.of(1)));
        MINER.addPurchasable(new PurchasableObtainedMaterial(1000, HFMining.ELEVATOR.getStackFromEnum(Elevator.JUNK), Logs.of(3), Copper.of(2), Adamantite.of(1)).setNote(HFNotes.ELEVATOR));
        MINER.addPurchasable(new PurchasableObtained(200, HFMining.MATERIALS.getStackFromEnum(Material.COPPER)));
        MINER.addPurchasable(new PurchasableObtained(300, HFMining.MATERIALS.getStackFromEnum(Material.SILVER)));
        MINER.addPurchasable(new PurchasableObtained(400, HFMining.MATERIALS.getStackFromEnum(Material.GOLD)));
        MINER.addPurchasable(new PurchasableOre(150, new ItemStack(Items.COAL)));
        MINER.addPurchasable(new PurchasableOre(400, new ItemStack(Items.IRON_INGOT)));
        MINER.addPurchasable(new PurchasableOre(600, new ItemStack(Items.GOLD_INGOT)));
        //Selling things to the mine
        MINER.addPurchasable(-10, new ItemStack(Items.COAL, 1, 1), 10);
        MINER.addPurchasable(-6, new ItemStack(Items.GOLD_NUGGET), 10);
        MINER.addPurchasable(-10, new ItemStack(Items.COAL), 10);
        MINER.addPurchasable(-20, new ItemStack(Items.QUARTZ), 10);
        MINER.addPurchasable(-40, new ItemStack(Items.IRON_INGOT), 8);
        MINER.addPurchasable(-60, new ItemStack(Items.GOLD_INGOT), 5);
        MINER.setSpecialRules(new SpecialRulesQuest(Quests.BRANDON_MEET));
        MINER.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000);
        MINER.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }

    private static void registerPoultry() {
        POULTRY.addPurchasable(new Purchasable(50, HFAnimals.TOOLS.getStackFromEnum(CHICKEN_FEED)).addTooltip("tool.chicken.feed"));
        POULTRY.addPurchasable(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        POULTRY.addPurchasable(30, HFAnimals.TREATS.getStackFromEnum(Treat.CHICKEN));
        POULTRY.addPurchasable(new PurchasableEntity(EntityHarvestChicken.class, 1500, HFAnimals.ANIMAL.getStackFromEnum(CHICKEN)).setNote(HFNotes.CHICKEN_CARE));
        POULTRY.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        POULTRY.addPurchasable(250, new ItemStack(Items.NAME_TAG));
        POULTRY.addPurchasable(500, HFAnimals.TRAY.getStackFromEnum(FEEDER_EMPTY), 3);
        POULTRY.addPurchasable(1000, HFAnimals.TRAY.getStackFromEnum(NEST_EMPTY), 3);
        POULTRY.addPurchasable(5000, HFAnimals.SIZED.getStackFromEnum(INCUBATOR), 1);
        POULTRY.setSpecialRules(new SpecialRulesQuest(Quests.ASHLEE_MEET));
        POULTRY.addOpening(MONDAY, 6000, 12000).addOpening(TUESDAY, 6000, 12000).addOpening(WEDNESDAY, 6000, 12000);
        POULTRY.addOpening(THURSDAY, 6000, 12000).addOpening(FRIDAY, 6000, 12000).addOpening(SATURDAY, 6000, 12000);
    }

    private static void registerSupermarket() {
        Crop.REGISTRY.values().stream().filter(crop -> crop != Crop.NULL_CROP && crop.getSeedCost() > 0)
                .forEachOrdered(crop -> SUPERMARKET.addPurchasable(new PurchasableCropSeeds(crop)));

        SUPERMARKET.addPurchasable(500, HFTools.HOE.getStack(ToolTier.BASIC), 1);
        SUPERMARKET.addPurchasable(500, HFTools.SICKLE.getStack(ToolTier.BASIC), 1);
        SUPERMARKET.addPurchasable(1000, HFTools.WATERING_CAN.getStack(ToolTier.BASIC), 1);
        SUPERMARKET.addPurchasable(new Purchasable(2000, HFTools.AXE.getStack(ToolTier.BASIC)).setStock(1).setNote(HFNotes.AXE));
        SUPERMARKET.addPurchasable(new Purchasable(2000, HFTools.HAMMER.getStack(ToolTier.BASIC)).setStock(1).setNote(HFNotes.HAMMER));

        //TODO: Reenable in 1.0 when I readd marriage
        //SUPERMARKET.addPurchasable(new PurchasableBlueFeather(1000, HFNPCs.TOOLS.getStackFromEnum(BLUE_FEATHER)));
        SUPERMARKET.addPurchasable(RICEBALL.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(RICEBALL));
        SUPERMARKET.addPurchasable(OIL.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(OIL));
        SUPERMARKET.addPurchasable(FLOUR.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(FLOUR));
        SUPERMARKET.addPurchasable(CURRY_POWDER.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(CURRY_POWDER));
        SUPERMARKET.addPurchasable(DUMPLING_POWDER.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(DUMPLING_POWDER));
        SUPERMARKET.addPurchasable(CHOCOLATE.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(CHOCOLATE));
        SUPERMARKET.addPurchasable(WINE.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(WINE));
        SUPERMARKET.addPurchasable(SALT.getCost(), HFCooking.INGREDIENTS.getStackFromEnum(SALT));
        SUPERMARKET.addPurchasable(SALT.getCost(), new ItemStack(Items.SUGAR));
        SUPERMARKET.setSpecialRules(new SpecialRulesQuest(Quests.JENNI_MEET));
        SUPERMARKET.addOpening(MONDAY, 9000, 17000).addOpening(TUESDAY, 9000, 17000).addOpening(THURSDAY, 9000, 17000);
        SUPERMARKET.addOpening(FRIDAY, 9000, 17000).addOpening(SATURDAY, 11000, 15000);
        SUPERMARKET.addConditionalOpening((w,e,i) -> TownHelper.getClosestTownToEntity(e, false).getQuests().getFinished().contains(Quests.OPEN_WEDNESDAYS), WEDNESDAY, 9000, 17000);
    }

    private static void registerTackleshop() {
        BAITSHOP.addPurchasable(new Purchasable(Junk.BAIT.getCost(), HFFishing.JUNK.getStackFromEnum(Junk.BAIT)).addTooltip("junk.bait"));
        BAITSHOP.addPurchasable(1000L, HFFishing.FISHING_ROD.getStack(ToolTier.BASIC), 1);
        BAITSHOP.addPurchasable(new Purchasable(500L, HFFishing.AQUATIC_BLOCKS.getStackFromEnum(Aquatic.TRAP)).setStock(10).addTooltip("aquatic.trap"));
        BAITSHOP.addPurchasable(new PurchasableMaterials(3000L, HFFishing.FLOATING_BLOCKS.getStackFromEnum(Floating.HATCHERY), Logs.of(8), String.of(6)) {
            @Override
            public boolean canList(World world, EntityPlayer player) {
                return HFApi.quests.hasCompleted(Quests.SELL_HATCHERY, player);
            }
        }.setStock(1).addTooltip("floating.hatchery"));


        //Selling things to the carpenter
        BAITSHOP.addPurchasable(-100, new ItemStack(Items.PRISMARINE_SHARD), 5);
        BAITSHOP.addPurchasable(-150, new ItemStack(Items.PRISMARINE_CRYSTALS), 3);
        BAITSHOP.addPurchasable(-10, new ItemStack(Items.FISH, 1, 0), 20);
        BAITSHOP.addPurchasable(-30, new ItemStack(Items.FISH, 1, 1), 15);
        BAITSHOP.addPurchasable(-50, new ItemStack(Items.FISH, 1, 2), 10);
        BAITSHOP.addPurchasable(-100, new ItemStack(Items.FISH, 1, 3), 5);
        for (Fish fish: Fish.values()) {
            long sell = (fish.getSellValue(fish.getLengthFromSizeOfFish(MEDIUM_FISH))) - fish.getSellValue(fish.getLengthFromSizeOfFish(MEDIUM_FISH)) % 10;
            BAITSHOP.addPurchasable(new PurchasableObtained(-sell, HFFishing.FISH.getStackFromEnum(fish)).setStock(100));
        }

        BAITSHOP.setSpecialRules(new SpecialRulesQuest(Quests.JACOB_MEET));
        BAITSHOP.addOpening(TUESDAY, 13000, 19000).addOpening(WEDNESDAY, 13000, 19000).addOpening(THURSDAY, 13000, 19000).addOpening(FRIDAY, 13000, 19000);
    }

    private static void registerTrader() {
        TRADER.addPurchasable(-60, new ItemStack(Items.MILK_BUCKET), 3);
        TRADER.addPurchasable(-30, new ItemStack(Items.EGG), 10);
        TRADER.addPurchasable(-120, new ItemStack(Items.LEATHER), 3);
        TRADER.addPurchasable(-50, new ItemStack(Items.FEATHER), 8);
        TRADER.addPurchasable(-80, new ItemStack(Items.RABBIT_HIDE), 5);
        TRADER.addPurchasable(-20, new ItemStack(Items.STRING), 10);
        for (int i = 0; i < 16; i++) {
            TRADER.addPurchasable(-150, new ItemStack(Blocks.WOOL, 1, 0), 3);
        }

        TRADER.addOpening(MONDAY, 6000, 10000).addOpening(TUESDAY, 6000, 10000).addOpening(WEDNESDAY, 6000, 10000).addOpening(THURSDAY, 6000, 10000);
        TRADER.addOpening(FRIDAY, 6000, 10000).addOpening(SATURDAY, 6000, 10000).addOpening(SUNDAY, 6000, 10000);
        HFNPCs.TRADER.setHasInfo(null); //Remove the opening times
    }

    private static void registerCooking() {
        COOKING_FESTIVAL_FOOD.addPurchasable(5000L, HFCore.STORAGE.getStackFromEnum(Storage.BASKET));
        COOKING_FESTIVAL_FOOD.addPurchasable(50, new ItemStack(Items.FISH));
        COOKING_FESTIVAL_FOOD.addPurchasable(100, HFGathering.NATURE.getStackFromEnum(NaturalBlock.MINT));
        COOKING_FESTIVAL_FOOD.addPurchasable(150, HFGathering.NATURE.getStackFromEnum(NaturalBlock.CHAMOMILE));
        COOKING_FESTIVAL_FOOD.addPurchasable(200, HFGathering.NATURE.getStackFromEnum(NaturalBlock.LAVENDAR));
        COOKING_FESTIVAL_FOOD.addPurchasable(1000, HFGathering.NATURE.getStackFromEnum(NaturalBlock.MATSUTAKE));
        COOKING_FESTIVAL_FOOD.addPurchasable(150, new ItemStack(Blocks.BROWN_MUSHROOM));
        COOKING_FESTIVAL_FOOD.addPurchasable(300, HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.SMALL));
        COOKING_FESTIVAL_FOOD.addPurchasable(500, HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.SMALL));
        COOKING_FESTIVAL_FOOD.addPurchasable(HFCrops.WHEAT.getSellValue() * 4, new ItemStack(Items.BREAD));
        COOKING_FESTIVAL_FOOD.addPurchasable(new PurchasableCrop(HFCrops.CUCUMBER));
        COOKING_FESTIVAL_FOOD.addPurchasable(new PurchasableCrop(HFCrops.TOMATO));
        COOKING_FESTIVAL_FOOD.addPurchasable(new PurchasableCrop(HFCrops.ONION));
        COOKING_FESTIVAL_FOOD.addPurchasable(new PurchasableCrop(HFCrops.CORN));
        COOKING_FESTIVAL_FOOD.addPurchasable(new PurchasableCrop(HFCrops.EGGPLANT));
        COOKING_FESTIVAL_FOOD.addPurchasable(new PurchasableCrop(HFCrops.SPINACH));
        COOKING_FESTIVAL_FOOD.addPurchasable(new PurchasableCrop(HFCrops.SWEET_POTATO));
    }

    private static void registerRecipes() {
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "juice_vegetable")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "sushi")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "sashimi")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "sashimi_chirashi")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "cucumber_pickled")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "juice_tomato")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "cornflakes")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "ketchup")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "stew_pumpkin")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "rice_fried")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "doria")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "juice_fruit")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "salad_herb")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "soup_herb")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "sandwich_herb")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "sweet_potatoes")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "eggplant_happy")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "sandwich")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "sandwich_fruit")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "latte_fruit")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "spinach_boiled")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "riceballs_toasted")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "omelet")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "egg_boiled")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "egg_overrice")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "juice_mix")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "pancake")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "rice_matsutake")));
        COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, "rice_mushroom")));

        //Add all the recipes the player has learnt from friendship to the list
        for (Quest quest: Quest.REGISTRY) {
            if (quest instanceof QuestRecipe) {
                for (java.lang.String recipe : ((QuestRecipe) quest).recipe) {
                    COOKING_FESTIVAL_RECIPES.addPurchasable(new PurchasableRecipe(new ResourceLocation(MODID, recipe)) {
                        @Override
                        public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
                            return HFApi.quests.hasCompleted(quest, player);
                        }
                    }.setStock(1));
                }
            }
        }
    }

    private static void registerCowBargains() {
        COW_FESTIVAL_BARGAINS.addPurchasable(100, HFAnimals.TREATS.getStackFromEnum(Treat.COW, 16));
        COW_FESTIVAL_BARGAINS.addPurchasable(500, HFCrops.GRASS.getCropStack(16));
        COW_FESTIVAL_BARGAINS.addPurchasable(1000, HFAnimals.TROUGH.getStackFromEnum(Trough.WOOD, 3));
        COW_FESTIVAL_BARGAINS.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(Tool.MEDICINE, 2));
        COW_FESTIVAL_BARGAINS.addPurchasable(1500, HFAnimals.TOOLS.getStackFromEnum(Tool.MIRACLE_POTION));
        COW_FESTIVAL_BARGAINS.addPurchasable(500, HFAnimals.TOOLS.getStackFromEnum(Tool.BRUSH));
        COW_FESTIVAL_BARGAINS.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(Tool.MILKER));
        COW_FESTIVAL_BARGAINS.addPurchasable(100, new ItemStack(Items.NAME_TAG));
    }

    private static void registerDairyQueen() {
        COW_FESTIVAL_DAIRY_QUEEN.addPurchasable(new PurchasableMeal(150, new ResourceLocation(MODID, "ice_cream")).setStock(20));
        COW_FESTIVAL_DAIRY_QUEEN.addPurchasable(new PurchasableMeal(100, new ResourceLocation(MODID, "milk_hot")).setStock(20));
        COW_FESTIVAL_DAIRY_QUEEN.addPurchasable(new PurchasableMeal(120, new ResourceLocation(MODID, "chocolate_hot")).setStock(10));
        COW_FESTIVAL_DAIRY_QUEEN.addPurchasable(new PurchasableMeal(50, new ResourceLocation(MODID, "cornflakes")).setStock(10));
        COW_FESTIVAL_DAIRY_QUEEN.addPurchasable(new PurchasableMeal(200, new ResourceLocation(MODID, "dinnerroll")).setStock(5));
        COW_FESTIVAL_DAIRY_QUEEN.addPurchasable(new PurchasableMeal(150, new ResourceLocation(MODID, "bun_jam")).setStock(5));
        COW_FESTIVAL_DAIRY_QUEEN.addPurchasable(new PurchasableMeal(300, new ResourceLocation(MODID, "doughnut")).setStock(5));
        COW_FESTIVAL_DAIRY_QUEEN.addPurchasable(new PurchasableMeal(140, new ResourceLocation(MODID, "milk_strawberry")).setStock(8));
    }

    private static void registerSaleSheep() {
        SHEEP_FESTIVAL_SALE.addPurchasable(100, HFAnimals.TREATS.getStackFromEnum(Treat.SHEEP, 16));
        SHEEP_FESTIVAL_SALE.addPurchasable(500, HFCrops.GRASS.getCropStack(16));
        SHEEP_FESTIVAL_SALE.addPurchasable(1000, HFAnimals.TROUGH.getStackFromEnum(Trough.WOOD, 3));
        SHEEP_FESTIVAL_SALE.addPurchasable(1000, HFAnimals.TOOLS.getStackFromEnum(Tool.MEDICINE, 2));
        SHEEP_FESTIVAL_SALE.addPurchasable(1500, HFAnimals.TOOLS.getStackFromEnum(Tool.MIRACLE_POTION));
        SHEEP_FESTIVAL_SALE.addPurchasable(500, HFAnimals.TOOLS.getStackFromEnum(Tool.BRUSH));
        SHEEP_FESTIVAL_SALE.addPurchasable(1000, new ItemStack(Items.SHEARS));
        SHEEP_FESTIVAL_SALE.addPurchasable(100, new ItemStack(Items.NAME_TAG));
    }

    public static ItemStack getWoolyArmor(Item item, java.lang.String name) {
        ItemStack stack = new ItemStack(item);
        stack.setStackDisplayName(name);
        ((ItemArmor)item).setColor(stack, 0xFFFFFF);
        return stack;
    }

    private static void registerGrannysKnits() {
        SHEEP_FESTIVAL_KNITTNG.addPurchasable(50, new ItemStack(Items.STRING), 16);
        SHEEP_FESTIVAL_KNITTNG.addPurchasable(200, new ItemStack(Blocks.WOOL), 16);
        SHEEP_FESTIVAL_KNITTNG.addPurchasable(500, getWoolyArmor(Items.LEATHER_HELMET, "Wooly Hat"), 1);
        SHEEP_FESTIVAL_KNITTNG.addPurchasable(800, getWoolyArmor(Items.LEATHER_CHESTPLATE, "Wooly Coat"), 1);
        SHEEP_FESTIVAL_KNITTNG.addPurchasable(700, getWoolyArmor(Items.LEATHER_LEGGINGS, "Wooly Pants"), 1);
        SHEEP_FESTIVAL_KNITTNG.addPurchasable(400, getWoolyArmor(Items.LEATHER_BOOTS, "Wooly Boots"), 1);
        SHEEP_FESTIVAL_KNITTNG.addPurchasable(300, new ItemStack(Items.COOKED_MUTTON), 20);
    }

    private static void registerMinecraftFriedChicken() {
        CHICKEN_FESTIVAL_MFC.addPurchasable(250, new ItemStack(Items.COOKED_CHICKEN), 20);
        CHICKEN_FESTIVAL_MFC.addPurchasable(new PurchasableMeal(150, new ResourceLocation(MODID, "fries_french")).setStock(20));
        CHICKEN_FESTIVAL_MFC.addPurchasable(new PurchasableMeal(0, new ResourceLocation(MODID, "ketchup")).setStock(20));
        CHICKEN_FESTIVAL_MFC.addPurchasable(new PurchasableMeal(50, new ResourceLocation(MODID, "egg_boiled")).setStock(5));
        CHICKEN_FESTIVAL_MFC.addPurchasable(new PurchasableMeal(100, new ResourceLocation(MODID, "egg_scrambled")).setStock(5));
        CHICKEN_FESTIVAL_MFC.addPurchasable(new PurchasableMeal(200, new ResourceLocation(MODID, "juice_orange")).setStock(8));
        CHICKEN_FESTIVAL_MFC.addPurchasable(new PurchasableMeal(100, new ResourceLocation(MODID, "juice_apple")).setStock(8));
    }

    private static Shop newHolidayShop(ResourceLocation resource, @Nullable NPC npc, Festival festival) {
        Shop shop = new Shop(resource).setOpensOnHolidays();
        if (npc != null) {
            if (npc instanceof NPCHolidayStore) ((NPCHolidayStore)npc).addHolidayShop(festival, shop);
            else if (npc instanceof NPCHolidayStoreSpecial) ((NPCHolidayStoreSpecial)npc).addHolidayShop(festival, shop);
        }

        return shop.addOpening(MONDAY, 6000, 18000).addOpening(TUESDAY, 6000, 18000).addOpening(WEDNESDAY, 6000, 18000).addOpening(THURSDAY, 6000, 18000).addOpening(FRIDAY, 6000, 18000).addOpening(SATURDAY, 6000, 18000).addOpening(SUNDAY, 6000, 18000);
    }

    private static Shop newShop(ResourceLocation resource, @Nullable NPC npc) {
        Shop shop = new Shop(resource);
        if (npc != null) {
            npc.setShop(shop);
        }

        return shop;
    }

    public static boolean TWENTY_FOUR_HOUR_SHOPPING;

    public static void configure() {
        TWENTY_FOUR_HOUR_SHOPPING = getBoolean("Shops are open all the time", false);
    }
}