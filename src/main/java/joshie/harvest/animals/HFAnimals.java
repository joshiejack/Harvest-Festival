package joshie.harvest.animals;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.block.BlockSizedStorage;
import joshie.harvest.animals.block.BlockTray;
import joshie.harvest.animals.block.BlockTrough;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.item.ItemAnimalSpawner;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.animals.item.ItemAnimalTool;
import joshie.harvest.animals.item.ItemAnimalTreat;
import joshie.harvest.animals.render.*;
import joshie.harvest.animals.tile.TileFeeder;
import joshie.harvest.animals.tile.TileIncubator;
import joshie.harvest.animals.tile.TileTrough;
import joshie.harvest.animals.type.AnimalChicken;
import joshie.harvest.animals.type.AnimalCow;
import joshie.harvest.animals.type.AnimalSheep;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.lib.Sizeable;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.crops.HFCrops;
import net.minecraft.init.Items;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.animals.AnimalRegistry.registerFoodsAsType;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.CHICKEN_FEED;
import static joshie.harvest.api.HFApi.animals;
import static joshie.harvest.api.animals.AnimalFoodType.*;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;
import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;
import static net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity;

@HFLoader
public class HFAnimals {
    //Animal Items
    public static final ItemAnimalSpawner ANIMAL = new ItemAnimalSpawner().register("animal");
    public static final ItemAnimalTool TOOLS = new ItemAnimalTool().register("tool_animal");
    public static final ItemAnimalTreat TREATS = new ItemAnimalTreat().register("treat");

    //Animal Blocks
    public static final BlockTrough TROUGH = new BlockTrough().register("trough");
    public static final BlockSizedStorage SIZED = new BlockSizedStorage().register("sized");
    public static final BlockTray TRAY = new BlockTray().register("tray");

    //Sizeables
    public static final Sizeable EGG = SizeableRegistry.INSTANCE.registerSizeable("egg", 50, 60, 80);
    public static final Sizeable MILK = SizeableRegistry.INSTANCE.registerSizeable("milk", 100, 150, 200);
    public static final Sizeable MAYONNAISE = SizeableRegistry.INSTANCE.registerSizeable("mayonnaise", 300, 400, 500);
    public static final Sizeable WOOL = SizeableRegistry.INSTANCE.registerSizeable("wool", 100, 400, 500);

    @SuppressWarnings("unchecked")
    public static void preInit() {
        registerModEntity(EntityHarvestCow.class, "cow", EntityIDs.COW, HarvestFestival.instance, 150, 3, true);
        registerModEntity(EntityHarvestSheep.class, "sheep", EntityIDs.SHEEP, HarvestFestival.instance, 150, 3, true);
        registerModEntity(EntityHarvestChicken.class, "chicken", EntityIDs.CHICKEN, HarvestFestival.instance, 150, 3, true);
        registerFoodsAsType(CHICKEN, Items.CHICKEN, Items.COOKED_CHICKEN);
        registerFoodsAsType(FISH, Items.FISH, Items.COOKED_FISH);
        registerFoodsAsType(FRUIT, Items.APPLE, Items.MELON);
        registerFoodsAsType(GRASS, Items.WHEAT);
        registerFoodsAsType(REDMEAT, Items.PORKCHOP, Items.BEEF, Items.COOKED_PORKCHOP, Items.COOKED_BEEF);
        registerFoodsAsType(SEED, Items.MELON_SEEDS, Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS);
        registerFoodsAsType(VEGETABLE, Items.CARROT);
        animals.registerFoodAsType(TOOLS.getStackFromEnum(CHICKEN_FEED), SEED);
        animals.registerType("cow", new AnimalCow());
        animals.registerType("sheep", new AnimalSheep());
        animals.registerType("chicken", new AnimalChicken());
        registerTiles(TileIncubator.class, TileTrough.class, TileFeeder.class);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestCow.class, RenderHarvestCow:: new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestSheep.class, RenderHarvestSheep:: new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestChicken.class, RenderHarvestChicken:: new);
        RegistryHelper.registerEntityRenderer(ANIMAL, AnimalItemRenderer.INSTANCE);
        AnimalItemRenderer.INSTANCE.register(Spawner.COW, "cow_adult", new ModelHarvestCow.Adult());
        AnimalItemRenderer.INSTANCE.register(Spawner.SHEEP, "sheep_adult", new ModelHarvestSheep.Wooly());
        AnimalItemRenderer.INSTANCE.register(Spawner.CHICKEN, "chicken_adult", new ModelHarvestChicken.Adult());
    }

    public static void init() {
        for (Crop crop : CropRegistry.REGISTRY.getValues()) {
            if (crop == HFCrops.NULL_CROP) continue;
            animals.registerFoodAsType(crop.getCropStack(), crop.getFoodType());
        }
    }

    //Configuration
    public static boolean CAN_SPAWN;
    public static boolean DISABLE_SPAWN_CHICKEN;
    public static boolean PICKUP_CHICKENS;
    public static int MAX_LITTER_SIZE;
    public static int LITTER_EXTRA_CHANCE;
    public static int AGING_TIMER;
    public static int PREGNANCY_TIMER;
    public static int CHICKEN_TIMER;
    public static boolean OP_ANIMALS;

    public static void configure() {
        CAN_SPAWN = getBoolean("Enable animal natural spawning", false);
        DISABLE_SPAWN_CHICKEN = getBoolean("Disable vanilla chickens from eggs", false);
        PICKUP_CHICKENS = getBoolean("Enable placing of chickens on your head", true);
        PREGNANCY_TIMER = getInteger("Pregnancy > Number of days", 7);
        CHICKEN_TIMER = HFAnimals.PREGNANCY_TIMER / 2;
        MAX_LITTER_SIZE = getInteger("Pregnancy > Max litter size", 5);
        LITTER_EXTRA_CHANCE = getInteger("Pregnancy > Chance of extra birth", 4);
        AGING_TIMER = getInteger("Number of days animals take to mature", 14);
        OP_ANIMALS = getBoolean("Old Mcdonald had a farm", false);
    }
}