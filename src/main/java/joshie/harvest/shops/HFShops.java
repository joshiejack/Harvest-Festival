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
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockSprinkler.Sprinkler;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockLadder.Ladder;
import joshie.harvest.mining.block.BlockStone;
import joshie.harvest.mining.block.BlockStone.Type;
import joshie.harvest.mining.item.ItemMiningTool.MiningTool;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.shops.purchaseable.*;
import joshie.harvest.tools.HFTools;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
import static joshie.harvest.npc.item.ItemNPCTool.NPCTool.BLUE_FEATHER;

@HFLoader
public class HFShops {
    public static IShop BARN;
    public static IShop BLACKSMITH;
    public static IShop CAFE;
    public static IShop CARPENTER;
    public static IShop POULTRY;
    public static IShop SUPERMARKET;
    public static IShop MINER;

    public static void postInit() {
        registerBarn();
        registerBlacksmith();
        registerCafe();
        registerCarpenter();
        registerPoultry();
        registerSupermarket();
        registerMiner();
    }
    
    private static void registerBarn() {
        BARN = HFApi.shops.newShop(new ResourceLocation(MODID, "barn"), HFNPCs.ANIMAL_OWNER);
        BARN.addItem(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        BARN.addItem(30, HFAnimals.TREATS.getStackFromEnum(Treat.COW));
        BARN.addItem(30, HFAnimals.TREATS.getStackFromEnum(Treat.SHEEP));
        BARN.addItem(20, HFCrops.GRASS.getCropStack(1));
        BARN.addItem(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        BARN.addItem(new PurchaseableEntity(EntityHarvestSheep.class, 4000, HFAnimals.ANIMAL.getStackFromEnum(SHEEP), true));
        BARN.addItem(new PurchaseableEntity(EntityHarvestCow.class, 5000, HFAnimals.ANIMAL.getStackFromEnum(COW), true));
        BARN.addItem(3000, HFAnimals.TOOLS.getStackFromEnum(MIRACLE_POTION));
        BARN.addItem(500, HFAnimals.TROUGH.getStackFromEnum(WOOD));

        BARN.addOpening(MONDAY, 10000, 15000).addOpening(TUESDAY, 10000, 15000).addOpening(WEDNESDAY, 10000, 15000);
        BARN.addOpening(THURSDAY, 10000, 15000).addOpening(FRIDAY, 10000, 15000).addOpening(SATURDAY, 10000, 15000);
    }

    private static void registerBlacksmith() {
        BLACKSMITH = HFApi.shops.newShop(new ResourceLocation(MODID, "blacksmith"), HFNPCs.TOOL_OWNER);
        BLACKSMITH.addItem(800, HFAnimals.TOOLS.getStackFromEnum(BRUSH));
        BLACKSMITH.addItem(2000, HFAnimals.TOOLS.getStackFromEnum(MILKER));
        BLACKSMITH.addItem(1800, new ItemStack(Items.SHEARS));

        //Allow the purchasing of special tools at the weekend, and special blocks
        BLACKSMITH.addItem(250, HFTools.HOE.getStack(ToolTier.BASIC));
        BLACKSMITH.addItem(250, HFTools.SICKLE.getStack(ToolTier.BASIC));
        BLACKSMITH.addItem(500, HFTools.WATERING_CAN.getStack(ToolTier.BASIC));
        BLACKSMITH.addItem(1000, HFTools.AXE.getStack(ToolTier.BASIC));
        BLACKSMITH.addItem(1000, HFTools.HAMMER.getStack(ToolTier.BASIC));
        BLACKSMITH.addItem(new PurchaseableWeekend(100, HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING)));
        BLACKSMITH.addItem(new PurchaseableYearWeekend(10000, HFCrops.SPRINKLER.getStackFromEnum(Sprinkler.WOOD), 1));

        BLACKSMITH.addOpening(SUNDAY, 10000, 16000).addOpening(MONDAY, 10000, 16000).addOpening(TUESDAY, 10000, 16000);
        BLACKSMITH.addOpening(WEDNESDAY, 10000, 16000).addOpening(FRIDAY, 10000, 16000).addOpening(SATURDAY, 10000, 16000);
    }

    private static void registerCafe() {
        CAFE = HFApi.shops.newShop(new ResourceLocation(MODID, "cafe"), HFNPCs.CAFE_OWNER);
        CAFE.addItem(0, new ItemStack(Items.POTIONITEM));

        CAFE.addItem(new PurchaseableMeal(500, new ResourceLocation(MODID, "salad")));
        CAFE.addItem(new PurchaseableMeal(400, new ResourceLocation(MODID, "cookies")));
        CAFE.addItem(new PurchaseableMeal(750, new ResourceLocation(MODID, "juice_pineapple")));
        CAFE.addItem(new PurchaseableMeal(250, new ResourceLocation(MODID, "corn_baked")));

        //Allow the purchasing of cookware at the weekends
        CAFE.addItem(new PurchaseableWeekend(25, new ItemStack(COOKBOOK)));
        CAFE.addItem(new PurchaseableWeekend(50, UTENSILS.getStackFromEnum(KNIFE)));
        CAFE.addItem(new PurchaseableWeekend(250, COOKWARE.getStackFromEnum(COUNTER)));
        CAFE.addItem(new PurchaseableWeekend(3000, COOKWARE.getStackFromEnum(FRIDGE)));
        CAFE.addItem(new PurchaseableWeekend(2500, COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addItem(new PurchaseableWeekend(1500, COOKWARE.getStackFromEnum(FRYING_PAN), COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addItem(new PurchaseableWeekend(1000, COOKWARE.getStackFromEnum(POT), COOKWARE.getStackFromEnum(OVEN_OFF)));
        CAFE.addItem(new PurchaseableWeekend(1200, COOKWARE.getStackFromEnum(MIXER), COOKWARE.getStackFromEnum(COUNTER)));

        //Add recipes for purchase
        CAFE.addItem(new PurchaseableRecipe(SPRING, MONDAY, new ResourceLocation(MODID, "juice_vegetable")));
        CAFE.addItem(new PurchaseableRecipe(SPRING, TUESDAY, new ResourceLocation(MODID, "sushi")));
        CAFE.addItem(new PurchaseableRecipe(SPRING, WEDNESDAY, new ResourceLocation(MODID, "sashimi")));
        CAFE.addItem(new PurchaseableRecipe(SPRING, THURSDAY, new ResourceLocation(MODID, "sashimi_chirashi")));
        CAFE.addItem(new PurchaseableRecipe(SPRING, FRIDAY, new ResourceLocation(MODID, "cucumber_pickled")));
        CAFE.addItem(new PurchaseableRecipe(SUMMER, SATURDAY, new ResourceLocation(MODID, "juice_tomato")));
        CAFE.addItem(new PurchaseableRecipe(SUMMER, SUNDAY, new ResourceLocation(MODID, "cornflakes")));
        CAFE.addItem(new PurchaseableRecipe(SUMMER, MONDAY, new ResourceLocation(MODID, "ketchup")));
        CAFE.addItem(new PurchaseableRecipe(SUMMER, TUESDAY, new ResourceLocation(MODID, "stew_pumpkin")));
        CAFE.addItem(new PurchaseableRecipe(SUMMER, THURSDAY, new ResourceLocation(MODID, "doria")));
        CAFE.addItem(new PurchaseableRecipe(AUTUMN, TUESDAY, new ResourceLocation(MODID, "eggplant_happy")));
        CAFE.addItem(new PurchaseableRecipe(AUTUMN, WEDNESDAY, new ResourceLocation(MODID, "sandwich")));
        CAFE.addItem(new PurchaseableRecipe(AUTUMN, SATURDAY, new ResourceLocation(MODID, "spinach_boiled")));
        CAFE.addItem(new PurchaseableRecipe(AUTUMN, SUNDAY, new ResourceLocation(MODID, "riceballs_toasted")));
        CAFE.addItem(new PurchaseableRecipe(WINTER, MONDAY, new ResourceLocation(MODID, "omelet")));
        CAFE.addItem(new PurchaseableRecipe(WINTER, TUESDAY, new ResourceLocation(MODID, "egg_boiled")));
        CAFE.addItem(new PurchaseableRecipe(WINTER, WEDNESDAY, new ResourceLocation(MODID, "egg_overrice")));
        CAFE.addItem(new PurchaseableRecipe(WINTER, FRIDAY, new ResourceLocation(MODID, "pancake")));

        CAFE.addOpening(MONDAY, 9500, 17000).addOpening(TUESDAY, 9500, 17000).addOpening(WEDNESDAY, 9500, 17000).addOpening(THURSDAY, 9500, 17000);
        CAFE.addOpening(FRIDAY, 9500, 17000).addOpening(SATURDAY, 9500, 17000).addOpening(SUNDAY, 9500, 17000);
    }

    private static void registerCarpenter() {
        CARPENTER = HFApi.shops.newShop(new ResourceLocation(MODID, "carpenter"), HFNPCs.BUILDER);
        for (BuildingImpl building : BuildingRegistry.REGISTRY.getValues()) {
            if (building.canPurchase()) {
                CARPENTER.addItem(new PurchaseableBuilding(building));
            }
        }

        CARPENTER.addItem(new PurchaseableBuilder(0, 16, 0, HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING)));
        CARPENTER.addItem(new PurchaseableBuilder(100, 0, 0, new ItemStack(Blocks.LOG)));
        CARPENTER.addItem(new PurchaseableBuilder(50, 0, 0, new ItemStack(Blocks.STONE)));
        CARPENTER.addOpening(MONDAY, 9000, 17500).addOpening(TUESDAY, 9000, 17500).addOpening(WEDNESDAY, 9000, 17500);
        CARPENTER.addOpening(THURSDAY, 9000, 17500).addOpening(FRIDAY, 9000, 17500).addOpening(SUNDAY, 9000, 17500);
    }

    private static void registerPoultry() {
        POULTRY = HFApi.shops.newShop(new ResourceLocation(MODID, "poultry"), HFNPCs.POULTRY);
        POULTRY.addItem(new PurchaseableEntity(EntityHarvestChicken.class, 1500, HFAnimals.ANIMAL.getStackFromEnum(CHICKEN), false));
        POULTRY.addItem(1000, HFAnimals.TOOLS.getStackFromEnum(MEDICINE));
        POULTRY.addItem(10, HFAnimals.TOOLS.getStackFromEnum(CHICKEN_FEED));
        POULTRY.addItem(10, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC));
        POULTRY.addItem(30, HFAnimals.TREATS.getStackFromEnum(Treat.CHICKEN));
        POULTRY.addItem(500, HFAnimals.TRAY.getStackFromEnum(FEEDER_EMPTY));
        POULTRY.addItem(1000, HFAnimals.TRAY.getStackFromEnum(NEST_EMPTY));
        POULTRY.addItem(7500, HFAnimals.SIZED.getStackFromEnum(INCUBATOR));

        POULTRY.addOpening(MONDAY, 5000, 11000).addOpening(TUESDAY, 5000, 11000).addOpening(WEDNESDAY, 5000, 11000);
        POULTRY.addOpening(THURSDAY, 5000, 11000).addOpening(FRIDAY, 5000, 11000).addOpening(SATURDAY, 5000, 11000);
    }

    private static void registerSupermarket() {
        SUPERMARKET = HFApi.shops.newShop(new ResourceLocation(MODID, "general"), HFNPCs.GS_OWNER);
        for (Crop crop : Crop.REGISTRY.getValues()) {
            if (crop != Crop.NULL_CROP) {
                SUPERMARKET.addItem(new PurchaseableCropSeeds(crop));
            }
        }

        SUPERMARKET.addItem(100, new ItemStack(Items.BREAD));
        SUPERMARKET.addItem(50, HFCooking.INGREDIENTS.getStackFromEnum(FLOUR));
        SUPERMARKET.addItem(100, HFCooking.INGREDIENTS.getStackFromEnum(CHOCOLATE));
        SUPERMARKET.addItem(50, HFCooking.INGREDIENTS.getStackFromEnum(OIL));
        SUPERMARKET.addItem(100, HFCooking.INGREDIENTS.getStackFromEnum(RICEBALL));
        SUPERMARKET.addItem(25, HFCooking.INGREDIENTS.getStackFromEnum(SALT));
        SUPERMARKET.addItem(new PurchaseableBlueFeather(1000, HFNPCs.TOOLS.getStackFromEnum(BLUE_FEATHER)));

        SUPERMARKET.addOpening(MONDAY, 9000, 17000).addOpening(TUESDAY, 9000, 17000).addOpening(THURSDAY, 9000, 17000);
        SUPERMARKET.addOpening(FRIDAY, 9000, 17000).addOpening(SATURDAY, 11000, 15000);
    }

    private static void registerMiner() {
        MINER = HFApi.shops.newShop(new ResourceLocation(MODID, "miner"), HFNPCs.MINER);
        MINER.addItem(new PurchaseableDecorative(1000, new ItemStack(HFMining.DIRT_DECORATIVE, 16, 0)));
        MINER.addItem(new PurchaseableDecorative(500, HFMining.LADDER.getStackFromEnum(Ladder.DECORATIVE)));
        MINER.addItem(150, HFMining.MINING_TOOL.getStackFromEnum(MiningTool.ESCAPE_ROPE));

        for (Type type: BlockStone.Type.values()) {
            if (type.isFake()) {
                MINER.addItem(new PurchaseableDecorative(1000, new ItemStack(HFMining.STONE, 16, type.ordinal())));
            }
        }

        MINER.addOpening(MONDAY, 11000, 16000).addOpening(TUESDAY, 11000, 16000).addOpening(WEDNESDAY, 11000, 16000); //You decide what time it will be open yoshie
        MINER.addOpening(THURSDAY, 11000, 16000).addOpening(FRIDAY, 11000, 16000).addOpening(SATURDAY, 11000, 16000);
    }

    public static boolean TWENTY_FOUR_HOUR_SHOPPING;

    public static void configure() {
        TWENTY_FOUR_HOUR_SHOPPING = getBoolean("Shops are open all the time", false);
    }
}