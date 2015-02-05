package joshie.harvestmoon.cooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import joshie.harvestmoon.cooking.registry.ICookingComponent;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemMeal;
import joshie.harvestmoon.util.SafeStack;
import joshie.harvestmoon.util.WildStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FoodRegistry {
    private static HashMap<String, ArrayList<ICookingComponent>> equivalents = new HashMap();
    private static HashMap<SafeStack, ICookingComponent> registry = new HashMap();
    private static final ArrayList<Recipe> recipes = new ArrayList();

    public static void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public static ItemStack getResult(Utensil utensil, ArrayList<ItemStack> iStacks, ArrayList<ItemStack> sStacks) {
        ArrayList<Ingredient> ingredients = new ArrayList();
        for (ItemStack stack : iStacks) {
            ingredients.add(getIngredient(stack));
        }

        ArrayList<Seasoning> seasonings = new ArrayList();
        for (ItemStack stack : sStacks) {
            seasonings.add(getSeasoning(stack));
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

        return Meal.BURNT;
    }

    public static ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public static void register(ItemStack stack, ICookingComponent component) {
        SafeStack safe = stack.getItemDamage() == OreDictionary.WILDCARD_VALUE ? new WildStack(stack) : new SafeStack(stack);
        FoodRegistry.registry.put(safe, component.addKey(safe));
    }

    public static Ingredient getIngredient(String string) {
        return Ingredient.ingredients.get(string);
    }

    public static Seasoning getSeasoning(String string) {
        return Seasoning.seasonings.get(string);
    }

    public static Ingredient getIngredient(ItemStack stack) {
        ICookingComponent result = registry.get(new SafeStack(stack));
        if (result == null) result = registry.get(new WildStack(stack));
        return (Ingredient) result;
    }

    public static Utensil getUtensil(ItemStack stack) {
        ICookingComponent result = registry.get(new SafeStack(stack));
        if (result == null) result = registry.get(new WildStack(stack));
        return (Utensil) result;
    }

    public static Seasoning getSeasoning(ItemStack stack) {
        ICookingComponent result = registry.get(new SafeStack(stack));
        if (result == null) result = registry.get(new WildStack(stack));
        return (Seasoning) result;
    }

    public static void addEquivalent(String string, ICookingComponent component) {
        ArrayList<ICookingComponent> list = equivalents.get(string);
        if (list == null) list = new ArrayList();
        list.add(component);
        equivalents.put(string, list);
    }

    public static ArrayList<ICookingComponent> getEquivalents(String name) {
        return equivalents.get(name);
    }
}
