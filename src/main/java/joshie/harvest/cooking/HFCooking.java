package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.cooking.IMealRecipe;
import joshie.harvest.cooking.render.MappingEvent;
import joshie.harvest.cooking.render.MealDefinition;
import joshie.harvest.core.helpers.ModelHelper;
import joshie.harvest.items.HFItems;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFCooking {
    public static void preInit() {
        HFApi.COOKING.registerRecipeHandler(new MayoRecipeHandler());
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(HFItems.MEAL, new MealDefinition());
        MinecraftForge.EVENT_BUS.register(new MappingEvent());
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        for (IMealRecipe recipe : HFApi.COOKING.getRecipes()) {
            IMeal meal = recipe.getMeal();
            ModelResourceLocation model = new ModelResourceLocation(meal.getResource(), "inventory");
            ModelBakery.registerItemVariants(HFItems.MEAL, model);
            MealDefinition.registerMeal(meal.getUnlocalizedName(), model);
        }

        for (Utensil utensil: Utensil.values()) {
            ModelResourceLocation model = ModelHelper.getModelForItem("meals/burnt" + utensil.name());
            ModelBakery.registerItemVariants(HFItems.MEAL, model);
            MealDefinition.registerBurnt(utensil.ordinal(), model);
        }
    }
}