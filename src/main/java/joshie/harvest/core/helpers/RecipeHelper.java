package joshie.harvest.core.helpers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.cooking.IMealRecipe;
import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class RecipeHelper {
    private static IMealRecipe addRecipe(String mealname, IUtensil utensil, int hunger, float saturation, float exhaustion, int eatTimer, ICookingIngredient... ingredients) {
        return HFApi.cooking.addMeal(new ResourceLocation(MODID, mealname), utensil, hunger, saturation, exhaustion, eatTimer, ingredients);
    }
    
    private static IMealRecipe addRecipe(String mealname, IUtensil utensil, int hunger, float saturation, float exhaustion, boolean drink, ICookingIngredient... ingredients) {
        int timer = drink ? 8:  4 + (int) (hunger * saturation * 4);
        IMealRecipe ret = addRecipe(mealname, utensil, hunger, saturation, exhaustion, timer, ingredients);
        return drink? ret.setIsDrink(): ret;
    }
    
    public static IMealRecipe addFryingPanRecipe(String mealname, int hunger, float saturation, float exhaustion, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.FRYING_PAN, hunger, saturation, exhaustion, false, ingredients);
    }
    
    public static IMealRecipe addMixerRecipe(String mealname, boolean isDrink, int hunger, float saturation, float exhaustion, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.MIXER, hunger, saturation, exhaustion, isDrink, ingredients);
    }
    
    public static IMealRecipe addMixerRecipe(String mealname, int hunger, float saturation, float exhaustion, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.MIXER, hunger, saturation, exhaustion, true, ingredients);
    }
    
    public static IMealRecipe addNoUtensilRecipe(String mealname, int hunger, float saturation, float exhaustion, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.COUNTER, hunger, saturation, exhaustion, false, ingredients);
    }
    
    public static IMealRecipe addPotRecipe(String mealname, boolean isDrink, int hunger, float saturation, float exhaustion, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.POT, hunger, saturation, exhaustion, isDrink, ingredients);
    }
    
    public static IMealRecipe addPotRecipe(String mealname, int hunger, float saturation, float exhaustion, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.POT, hunger, saturation, exhaustion, false, ingredients);
    }
    
    public static IMealRecipe addOvenRecipe(String mealname, int hunger, float saturation, float exhaustion, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.OVEN, hunger, saturation, exhaustion, false, ingredients);
    }
}
