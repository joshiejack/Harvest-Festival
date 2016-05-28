package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.tiles.*;
import joshie.harvest.buildings.render.PreviewRender;
import joshie.harvest.cooking.blocks.*;
import joshie.harvest.cooking.render.*;
import joshie.harvest.core.helpers.ModelHelper;
import joshie.harvest.core.util.base.BlockHFBaseEnum;
import joshie.harvest.items.HFItems;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;

public class HFCooking {
    //Cooking & Farming
    public static final BlockHFBaseEnum COOKWARE = new BlockCookware().setUnlocalizedName("cookware");

    public static void preInit() {
        HFApi.cooking.registerRecipeHandler(new MayoRecipeHandler());
        registerTiles(TileFridge.class, TileFryingPan.class, TileCounter.class, TileMarker.class, TileMixer.class, TileOven.class, TilePot.class);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(HFItems.MEAL, new MealDefinition());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMarker.class, new PreviewRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new SpecialRendererPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCounter.class, new SpecialRendererCounter());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMixer.class, new SpecialRendererMixer());
        MinecraftForge.EVENT_BUS.register(new MappingEvent());
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        for (Recipe recipe : FoodRegistry.REGISTRY.getValues()) {
            ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation(recipe.getRegistryName().getResourceDomain(), "meals/" + recipe.getRegistryName().getResourcePath()), "inventory");
            ModelBakery.registerItemVariants(HFItems.MEAL, model);
            MealDefinition.registerMeal(recipe, model);
        }

        for (Utensil utensil: Utensil.values()) {
            ModelResourceLocation model = ModelHelper.getModelForItem("meals/burnt" + utensil.name());
            ModelBakery.registerItemVariants(HFItems.MEAL, model);
            MealDefinition.registerBurnt(utensil.ordinal(), model);
        }
    }
}