package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.render.MappingEvent;
import joshie.harvest.cooking.render.MealDefinition;
import joshie.harvest.core.helpers.ModelHelper;
import joshie.harvest.items.HFItems;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFCooking {
    public static void preInit() {
        HFApi.cooking.registerRecipeHandler(new MayoRecipeHandler());
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(HFItems.MEAL, new MealDefinition());
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