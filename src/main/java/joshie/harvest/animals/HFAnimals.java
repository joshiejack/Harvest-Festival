package joshie.harvest.animals;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.blocks.TileNest;
import joshie.harvest.animals.blocks.TileTrough;
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
import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;
import static net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity;

public class HFAnimals {
    //Animal Items
    public static final ItemAnimalSpawner ANIMAL = new ItemAnimalSpawner().setUnlocalizedName("animal");
    public static final ItemAnimalTool TOOLS = new ItemAnimalTool().setUnlocalizedName("tool.animal");
    public static final ItemAnimalTreat TREATS = new ItemAnimalTreat().setUnlocalizedName("treat");

    //Sizeable Animal Products
    public static final Item EGG = HFApi.sizeable.createSizedItem("egg", 50, 60, 80);
    public static final Item MILK = HFApi.sizeable.createSizedItem("milk", 100, 150, 200);
    public static final Item MAYONNAISE = HFApi.sizeable.createSizedItem("mayonnaise", 300, 400, 500);
    public static final Item WOOL = HFApi.sizeable.createSizedItem("wool", 100, 400, 500);

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
        registerTiles(TileNest.class, TileTrough.class);
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
}