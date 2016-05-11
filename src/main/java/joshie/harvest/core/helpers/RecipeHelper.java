package joshie.harvest.core.helpers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.cooking.IMealRecipe;
import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class RecipeHelper {
    private static IMealRecipe addRecipe(String mealname, IUtensil utensil, int stamina, int fatigue, int hunger, float saturation, int eatTimer, ICookingIngredient... ingredients) {
        return HFApi.COOKING.addMeal(new ResourceLocation(MODID, mealname), utensil, stamina, fatigue, hunger, saturation, eatTimer, ingredients);
    }
    
    private static IMealRecipe addRecipe(String mealname, IUtensil utensil, int stamina, int fatigue, boolean drink, ICookingIngredient... ingredients) {
        int fill = ((ingredients.length/stamina) * -fatigue) + (stamina / 7);
        float saturation = (float) (((double)fill/(double)stamina) * ingredients.length);
        int timer = drink ? 8:  4 + ((ingredients.length +-fatigue) *4);
        if (saturation <= 0) saturation = 0.01F;
        
        if (fatigue != 0) {
            if (fill <= 0) fill = 1;            
            fill *= 1.5;
            saturation *= 1.5F;
        }
        
        IMealRecipe ret = addRecipe(mealname, utensil, stamina, fatigue, fill, saturation, timer, ingredients);
        return drink? ret.setIsDrink(): ret;
    }
    
    public static IMealRecipe addFryingPanRecipe(String mealname, int stamina, int fatigue, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.FRYING_PAN, stamina, fatigue, false, ingredients);
    }
    
    public static IMealRecipe addMixerRecipe(String mealname, boolean isDrink, int stamina, int fatigue, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.MIXER, stamina, fatigue, isDrink, ingredients);
    }
    
    public static IMealRecipe addMixerRecipe(String mealname, int stamina, int fatigue, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.MIXER, stamina, fatigue, true, ingredients);
    }
    
    public static IMealRecipe addNoUtensilRecipe(String mealname, int stamina, int fatigue, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.COUNTER, stamina, fatigue, false, ingredients);
    }
    
    public static IMealRecipe addPotRecipe(String mealname, boolean isDrink, int stamina, int fatigue, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.POT, stamina, fatigue, isDrink, ingredients);
    }
    
    public static IMealRecipe addPotRecipe(String mealname, int stamina, int fatigue, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.POT, stamina, fatigue, false, ingredients);
    }
    
    public static IMealRecipe addOvenRecipe(String mealname, int stamina, int fatigue, ICookingIngredient... ingredients) {
        return addRecipe(mealname, Utensil.OVEN, stamina, fatigue, false, ingredients);
    }
}
