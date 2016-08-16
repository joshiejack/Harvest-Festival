package joshie.harvest.mining;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.render.ModelHarvestChicken;
import joshie.harvest.animals.render.ModelHarvestCow;
import joshie.harvest.animals.render.ModelHarvestSheep;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.mining.blocks.*;
import joshie.harvest.mining.entity.EntityDarkChick;
import joshie.harvest.mining.entity.EntityDarkChicken;
import joshie.harvest.mining.entity.EntityDarkCow;
import joshie.harvest.mining.entity.EntityDarkSheep;
import joshie.harvest.mining.items.ItemDarkSpawner;
import joshie.harvest.mining.items.ItemDarkSpawner.DarkSpawner;
import joshie.harvest.mining.items.ItemMaterial;
import joshie.harvest.mining.loot.*;
import joshie.harvest.mining.render.BakedDirt;
import joshie.harvest.mining.render.RenderDarkChick;
import joshie.harvest.mining.render.RenderDarkChicken;
import joshie.harvest.mining.render.RenderDarkMob;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.world.DimensionType;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;
import static net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity;

@HFLoader
public class HFMining {
    public static final BlockOre ORE = new BlockOre().register("ore");
    public static final BlockStone STONE = new BlockStone().register("stone");
    public static final BlockDirt DIRT = new BlockDirt().setBlockUnbreakable().register("dirt");
    public static final BlockDirt DIRT_DECORATIVE = new BlockDirt().register("dirt_decorative");
    public static final BlockLadder LADDER = new BlockLadder().register("ladder");
    public static final BlockPortal PORTAL = new BlockPortal().setBlockUnbreakable().register("portal");
    public static final ItemMaterial MATERIALS = new ItemMaterial().register("materials");
    public static final ItemDarkSpawner DARK_SPAWNER = new ItemDarkSpawner().register("dark_spawner");
    public static DimensionType MINE_WORLD;

    public static void preInit() {
        MINE_WORLD = DimensionType.register("The Mine", "_hf_mine", MINING_ID, MiningProvider.class, false);
        DimensionManager.registerDimension(MINING_ID, MINE_WORLD);
        registerModEntity(EntityDarkCow.class, "dark_cow", EntityIDs.DARK_COW, HarvestFestival.instance, 80, 3, true);
        registerModEntity(EntityDarkSheep.class, "dark_sheep", EntityIDs.DARK_SHEEP, HarvestFestival.instance, 80, 3, true);
        registerModEntity(EntityDarkChicken.class, "dark_chicken", EntityIDs.DARK_CHICKEN, HarvestFestival.instance, 80, 3, true);
        registerModEntity(EntityDarkChick.class, "dark_chick", EntityIDs.DARK_CHICK, HarvestFestival.instance, 80, 3, true);
        LootConditionManager.registerCondition(new Between.Serializer());
        LootConditionManager.registerCondition(new EndsIn.Serializer());
        LootConditionManager.registerCondition(new Exact.Serializer());
        LootConditionManager.registerCondition(new MultipleOf.Serializer());
        LootConditionManager.registerCondition(new Obtained.Serializer());
        HFApi.tickable.registerDailyTickableBlock(DIRT, new MiningTicker());
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomStateMapper(DIRT, new BakedDirt.StateMapper());

        //Register the dark cow
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkCow.class, new IRenderFactory<EntityDarkCow>() {
            @Override
            public Render<? super EntityDarkCow> createRenderFor(RenderManager manager) {
                return new RenderDarkMob<>(manager, new ModelHarvestCow.Adult(), "dark_cow");
            }
        });

        //Dark Sheep
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkSheep.class, new IRenderFactory<EntityDarkSheep>() {
            @Override
            public Render<? super EntityDarkSheep> createRenderFor(RenderManager manager) {
                return new RenderDarkMob<>(manager, new ModelHarvestSheep.Wooly(), "dark_sheep");
            }
        });

        //Dark Chicken
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkChicken.class, new IRenderFactory<EntityDarkChicken>() {
            @Override
            public Render<? super EntityDarkChicken> createRenderFor(RenderManager manager) {
                return new RenderDarkChicken(manager);
            }
        });

        //Dark Chick
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkChick.class, new IRenderFactory<EntityDarkChick>() {
            @Override
            public Render<? super EntityDarkChick> createRenderFor(RenderManager manager) {
                return new RenderDarkChick(manager);
            }
        });

        RegistryHelper.registerEntityRendererItem("dark_cow", DARK_SPAWNER.getStackFromEnum(DarkSpawner.COW), new ModelHarvestCow.Adult());
        RegistryHelper.registerEntityRendererItem("dark_sheep", DARK_SPAWNER.getStackFromEnum(DarkSpawner.SHEEP), new ModelHarvestSheep.Wooly());
        RegistryHelper.registerEntityRendererItem("dark_chicken", DARK_SPAWNER.getStackFromEnum(DarkSpawner.CHICKEN), new ModelHarvestChicken.Adult());
        RegistryHelper.registerEntityRendererItem("dark_chick", DARK_SPAWNER.getStackFromEnum(DarkSpawner.CHICK), new ModelHarvestChicken.Child());
    }

    public static int MINING_ID;

    public static void configure() {
        MINING_ID = getInteger("Mining world ID", 4);
    }
}