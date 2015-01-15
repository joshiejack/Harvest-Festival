package joshie.harvestmoon.cooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import joshie.harvestmoon.cooking.registry.ICookingComponent;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemMeal;
import joshie.harvestmoon.util.SafeStack;
import net.minecraft.item.ItemStack;

public class FoodRegistry {
    private static HashMap<String, ArrayList<ICookingComponent>> equivalents = new HashMap();
    private static HashMap<SafeStack, ICookingComponent> registry = new HashMap();
    private static final ArrayList<Recipe> recipes = new ArrayList();
    
    public static void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }
    
    public static ItemStack getResult(Utensil utensil, List<Ingredient> ingredients, List<Seasoning> seasonings) {
        for(Recipe recipe: recipes) {
            Meal meal = recipe.getMeal(utensil, ingredients, seasonings);
            if(meal != null) {
                return ItemMeal.cook(new ItemStack(HMItems.meal), meal);
            }
        }
        
        return Meal.BURNT;
    }

    public static ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public static void register(ItemStack stack, ICookingComponent component) {
        FoodRegistry.registry.put(new SafeStack(stack), component.addKey(new SafeStack(stack)));
    }
    
    public static Ingredient getIngredient(SafeStack stack) {
        return (Ingredient) registry.get(stack);
    }

    public static Utensil getUtensil(SafeStack stack) {
        return (Utensil) registry.get(stack);
    }
    
    public static Seasoning getSeasoning(SafeStack stack) {
        return (Seasoning) registry.get(stack);
    }

    public static void addEquivalent(String string, ICookingComponent component) {
        ArrayList<ICookingComponent> list = equivalents.get(string);
        if(list == null) list = new ArrayList();
        list.add(component);
        equivalents.put(string, list);
    }
    
    public static ArrayList<ICookingComponent> getEquivalents(String name) {
        return equivalents.get(name);
    }
}
