package joshie.harvest.animals;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.blocks.*;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.item.ItemAnimalSpawner;
import joshie.harvest.animals.item.ItemAnimalTool;
import joshie.harvest.animals.item.ItemAnimalTreat;
import joshie.harvest.animals.render.ModelHarvestCow;
import joshie.harvest.animals.render.ModelHarvestSheep;
import joshie.harvest.animals.render.RenderHarvestAnimal;
import joshie.harvest.animals.type.AnimalChicken;
import joshie.harvest.animals.type.AnimalCow;
import joshie.harvest.animals.type.AnimalSheep;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.animals.AnimalRegistry.registerFoodsAsType;
import static joshie.harvest.animals.item.ItemAnimalSpawner.Spawner.COW;
import static joshie.harvest.animals.item.ItemAnimalSpawner.Spawner.SHEEP;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.CHICKEN_FEED;
import static joshie.harvest.api.HFApi.animals;
import static joshie.harvest.api.animals.AnimalFoodType.*;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;
import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;
import static net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity;

public class HFAnimals {
    //Animal Items
    public static final ItemAnimalSpawner ANIMAL = new ItemAnimalSpawner().register("animal");
    public static final ItemAnimalTool TOOLS = new ItemAnimalTool().register("tool_animal");
    public static final ItemAnimalTreat TREATS = new ItemAnimalTreat().register("treat");

    //Sizeable Animal Products
    public static final Item EGG = HFApi.sizeable.createSizedItem("egg", 50, 60, 80);
    public static final Item MILK = HFApi.sizeable.createSizedItem("milk", 100, 150, 200);
    public static final Item MAYONNAISE = HFApi.sizeable.createSizedItem("mayonnaise", 300, 400, 500);
    public static final Item WOOL = HFApi.sizeable.createSizedItem("wool", 100, 400, 500);

    //Animal Blocks
    public static final BlockTrough TROUGH = new BlockTrough().register("trough");
    public static final BlockSizedStorage SIZED = new BlockSizedStorage().register("sized");
    public static final BlockTray TRAY = new BlockTray().register("tray");

    public static void preInit() {
        registerModEntity(EntityHarvestCow.class, "cow", 5, HarvestFestival.instance, 150, 3, true);
        registerModEntity(EntityHarvestSheep.class, "sheep", 6, HarvestFestival.instance, 150, 3, true);
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
        //Register the cow
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestCow.class, new IRenderFactory<EntityHarvestCow>() {
            @Override
            public Render<? super EntityHarvestCow> createRenderFor(RenderManager manager) {
                return new RenderHarvestAnimal(manager, new ModelHarvestCow(), "cow");
            }
        });

        //Register the sheep
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestSheep.class, new IRenderFactory<EntityHarvestSheep>() {
            @Override
            public Render<? super EntityHarvestSheep> createRenderFor(RenderManager manager) {
                return new RenderHarvestAnimal(manager, new ModelHarvestSheep(), "sheep");
            }
        });

        RegistryHelper.registerEntityRendererItem(ANIMAL.getStackFromEnum(COW), new ModelHarvestCow());
        RegistryHelper.registerEntityRendererItem(ANIMAL.getStackFromEnum(SHEEP), new ModelHarvestSheep());
    }

    public static void init() {
        for (Crop crop : CropRegistry.REGISTRY.getValues()) {
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

    public static void configure() {
        CAN_SPAWN = getBoolean("Enable Animal Natural Spawning", false);
        DISABLE_SPAWN_CHICKEN = getBoolean("Disable Chickens from Eggs", false);
        PICKUP_CHICKENS = getBoolean("Enable Placing of Chickens on your head", true);
        PREGNANCY_TIMER = getInteger("Pregnancy: Number of days", 7);
        CHICKEN_TIMER = HFAnimals.PREGNANCY_TIMER / 2;
        MAX_LITTER_SIZE = getInteger("Pregnancy: Max litter size", 5);
        LITTER_EXTRA_CHANCE = getInteger("Pregnancy: Chance of extra birth", 4);
        AGING_TIMER = getInteger("Maturity: Number of days", 14);
    }
}