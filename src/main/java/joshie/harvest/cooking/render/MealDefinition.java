package joshie.harvest.cooking.render;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.cooking.Recipe;
import joshie.harvest.cooking.Utensil;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class MealDefinition implements ItemMeshDefinition {
    private static HashMap<Recipe, ModelResourceLocation> models = new HashMap<>();
    private static TIntObjectMap<ModelResourceLocation> burnt = new TIntObjectHashMap<>();
    public static void registerMeal(Recipe recipe, ModelResourceLocation resource) {
        models.put(recipe, resource);
    }

    public static void registerBurnt(int damage, ModelResourceLocation resource) {
        burnt.put(damage, resource);
    }

    public int getMetaFromStack(ItemStack stack) {
        if (stack.getItemDamage() >= 0 && stack.getItemDamage() < Utensil.values().length) {
            return stack.getItemDamage();
        }

        return 0;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        if (stack.hasTagCompound()) {
            ModelResourceLocation resource = models.get(FoodRegistry.REGISTRY.getObjectById(stack.getItemDamage()));
            if (resource != null) return resource;
        }

        return burnt.get(getMetaFromStack(stack));
    }
}
