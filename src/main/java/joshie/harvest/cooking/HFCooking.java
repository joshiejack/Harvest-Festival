package joshie.harvest.cooking;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.cooking.IMealRecipe;
import joshie.harvest.cooking.entity.EntityCookingItem;
import joshie.harvest.cooking.render.MealDefinition;
import joshie.harvest.core.helpers.ModelHelper;
import joshie.harvest.items.HFItems;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.cooking.HFIngredients.milk;

public class HFCooking {
    public static Fluid cookingOil;
    public static Fluid cookingMilk;

    public static void preInit() {
        EntityRegistry.registerModEntity(EntityCookingItem.class, "FakeItem", 1, HarvestFestival.instance, 80, 3, false);
        HFApi.COOKING.registerRecipeHandler(new MayoRecipeHandler());

        cookingOil = FluidRegistry.getFluid("oil.cooking");
        if (cookingOil == null) {
            cookingOil = new FluidCookingOil("oil.cooking");
            FluidRegistry.registerFluid(cookingOil);
        }

        cookingMilk = FluidRegistry.getFluid("milk");
        if (milk == null) {
            cookingMilk = new FluidCookingMilk("milk");
            FluidRegistry.registerFluid(cookingMilk);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(HFItems.MEAL, new MealDefinition());
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