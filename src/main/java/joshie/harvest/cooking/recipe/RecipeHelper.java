package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.api.cooking.Utensil.*;
import static joshie.harvest.core.lib.HFModInfo.MODID;

public class RecipeHelper {
    private static MealImpl addRecipe(String mealname, Utensil utensil, int hunger, float saturation, int eatTimer, Ingredient... ingredients) {
        return CookingAPI.INSTANCE.addMeal(new ResourceLocation(MODID, mealname), utensil, hunger, saturation, eatTimer, ingredients);
    }
    
    private static MealImpl addRecipe(String mealname, Utensil utensil, int hunger, float saturation, boolean drink, Ingredient... ingredients) {
        int timer = drink ? 8:  4 + (int) (hunger * saturation * 4);
        MealImpl ret = addRecipe(mealname, utensil, hunger, saturation, timer, ingredients);
        if(drink) ret.setIsDrink();
        return ret;
    }
    
    public static MealImpl addFryingPanRecipe(String mealname, int hunger, float saturation, Ingredient... ingredients) {
        return addRecipe(mealname, FRYING_PAN, hunger, saturation, false, ingredients);
    }

    public static void addFryingPanRecipe(ItemStack result, Ingredient... ingredients) {
        CookingAPI.INSTANCE.addRecipe(result, FRYING_PAN, ingredients);
    }
    
    public static MealImpl addMixerRecipe(String mealname, boolean isDrink, int hunger, float saturation, Ingredient... ingredients) {
        return addRecipe(mealname, MIXER, hunger, saturation, isDrink, ingredients);
    }
    
    public static MealImpl addMixerRecipe(String mealname, int hunger, float saturation, Ingredient... ingredients) {
        return addRecipe(mealname, MIXER, hunger, saturation, true, ingredients);
    }

    public static void addMixerRecipe(ItemStack result, Ingredient... ingredients) {
        CookingAPI.INSTANCE.addRecipe(result, MIXER, ingredients);
    }
    
    public static MealImpl addNoUtensilRecipe(String mealname, int hunger, float saturation, Ingredient... ingredients) {
        return addRecipe(mealname, COUNTER, hunger, saturation, false, ingredients);
    }

    public static void addNoUtensilRecipe(ItemStack result, Ingredient... ingredients) {
        CookingAPI.INSTANCE.addRecipe(result, COUNTER, ingredients);
    }
    
    public static MealImpl addPotRecipe(String mealname, boolean isDrink, int hunger, float saturation, Ingredient... ingredients) {
        return addRecipe(mealname, POT, hunger, saturation, isDrink, ingredients);
    }
    
    public static MealImpl addPotRecipe(String mealname, int hunger, float saturation, Ingredient... ingredients) {
        return addRecipe(mealname, POT, hunger, saturation, false, ingredients);
    }

    public static void addPotRecipe(ItemStack result, Ingredient... ingredients) {
        CookingAPI.INSTANCE.addRecipe(result, POT, ingredients);
    }
    
    public static MealImpl addOvenRecipe(String mealname, int hunger, float saturation, Ingredient... ingredients) {
        return addRecipe(mealname, OVEN, hunger, saturation,false, ingredients);
    }

    public static void addOvenRecipe(ItemStack result, Ingredient... ingredients) {
        CookingAPI.INSTANCE.addRecipe(result, OVEN, ingredients);
    }
}
