package joshie.harvestmoon.cooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import joshie.harvestmoon.api.ICookingComponent;
import joshie.harvestmoon.helpers.SafeStackHelper;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemMeal;
import joshie.harvestmoon.util.SafeStack;
import net.minecraft.item.ItemStack;

public class FoodRegistry {
    private static HashMap<String, ArrayList<ICookingComponent>> equivalents = new HashMap();
    private static HashMap<SafeStack, ArrayList<ICookingComponent>> registry = new HashMap();
    private static final ArrayList<Recipe> recipes = new ArrayList(250);

    public static void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public static ItemStack getResult(Utensil utensil, ArrayList<ItemStack> iStacks, ArrayList<ItemStack> sStacks) {
        ArrayList<Ingredient> ingredients = new ArrayList(20);
        for (ItemStack stack : iStacks) {
            ingredients.addAll(getIngredients(stack));
        }

        ArrayList<Seasoning> seasonings = new ArrayList(20);
        for (ItemStack stack : sStacks) {
            seasonings.addAll(getSeasonings(stack));
        }

        return getResult(utensil, ingredients, seasonings);
    }

    public static ItemStack getResult(Utensil utensil, List<Ingredient> ingredients, List<Seasoning> seasonings) {
        for (Recipe recipe : recipes) {
            Meal meal = recipe.getMeal(utensil, ingredients, seasonings);
            if (meal != null) {
                return ItemMeal.cook(new ItemStack(HMItems.meal), meal);
            }
        }

        ItemStack burnt = Meal.BURNT.copy();
        burnt.setItemDamage(utensil.ordinal());
        return burnt;
    }

    public static ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    private static ArrayList<ICookingComponent> getComponents(SafeStack safe) {
        ArrayList<ICookingComponent> components = FoodRegistry.registry.get(safe);
        return components == null ? new ArrayList(20) : components;
    }

    public static void register(SafeStack safe, ICookingComponent component) {
        ArrayList<ICookingComponent> components = getComponents(safe);
        components.add(component);
        FoodRegistry.registry.put(safe, components);
    }

    public static void register(ItemStack stack, ICookingComponent component) {
        SafeStack safe = SafeStackHelper.getSafeStackType(stack);
        ArrayList<ICookingComponent> components = getComponents(safe);
        components.add(component);
        FoodRegistry.registry.put(safe, components);
    }

    public static ArrayList<ICookingComponent> getCookingComponents(ItemStack stack) {
        return (ArrayList<ICookingComponent>) SafeStackHelper.getResult(stack, registry);
    }

    public static ArrayList<Ingredient> getIngredients(ItemStack stack) {
        ArrayList<ICookingComponent> components = getCookingComponents(stack);
        if (components == null) return null;
        ArrayList<Ingredient> ingredients = new ArrayList(20);
        for (ICookingComponent c : components) {
            if (c instanceof Ingredient) {
                ingredients.add((Ingredient) c);
            }
        }

        return ingredients.size() > 0 ? ingredients : null;
    }

    public static ArrayList<Seasoning> getSeasonings(ItemStack stack) {
        ArrayList<ICookingComponent> components = getCookingComponents(stack);
        if (components == null) return null;
        ArrayList<Seasoning> seasonings = new ArrayList(20);
        for (ICookingComponent c : components) {
            if (c instanceof Seasoning) {
                seasonings.add((Seasoning) c);
            }
        }

        return seasonings.size() > 0 ? seasonings : null;
    }
}
