package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.cooking.ICookingComponent;
import joshie.harvestmoon.api.cooking.IMealRecipe;
import joshie.harvestmoon.cooking.Meal;
import joshie.harvestmoon.cooking.Recipe;
import joshie.harvestmoon.cooking.Utensil;

public class RecipeHelper {
    private static IMealRecipe addRecipe(String mealname, int stamina, int fatigue, int hunger, float saturation, int eatTimer, ICookingComponent... ingredients) {
        return HMApi.COOKING.addRecipe(new Recipe(ingredients, new Meal(mealname, stamina, fatigue, hunger, saturation, eatTimer)));
    }
    
    private static IMealRecipe addRecipe(String mealname, int stamina, int fatigue, boolean drink, ICookingComponent... ingredients) {
        int fill = ((ingredients.length/stamina) * -fatigue) + (stamina / 7);
        float saturation = (float) (((double)fill/(double)stamina) * ingredients.length);
        int timer = drink ? 8:  4 + ((ingredients.length +-fatigue) *4);
        if (saturation <= 0) saturation = 0.01F;
        
        if (fatigue != 0) {
            if (fill <= 0) fill = 1;            
            fill *= 1.5;
            saturation *= 1.5F;
        }
        
        IMealRecipe ret = addRecipe(mealname, stamina, fatigue, fill, saturation, timer, ingredients);
        return drink? ret.setIsDrink(): ret;
    }
    
    public static IMealRecipe addFryingPanRecipe(String mealname, int stamina, int fatigue, ICookingComponent... ingredients) {
        return addRecipe(mealname, stamina, fatigue, false, ingredients).setRequiredTool(Utensil.FRYING_PAN);
    }
    
    public static IMealRecipe addMixerRecipe(String mealname, boolean isDrink, int stamina, int fatigue, ICookingComponent... ingredients) {
        return addRecipe(mealname, stamina, fatigue, isDrink, ingredients).setRequiredTool(Utensil.MIXER);
    }
    
    public static IMealRecipe addMixerRecipe(String mealname, int stamina, int fatigue, ICookingComponent... ingredients) {
        return addRecipe(mealname, stamina, fatigue, true, ingredients).setRequiredTool(Utensil.MIXER);
    }
    
    public static IMealRecipe addNoUtensilRecipe(String mealname, int stamina, int fatigue, ICookingComponent... ingredients) {
        return addRecipe(mealname, stamina, fatigue, false, ingredients).setRequiredTool(Utensil.KITCHEN);
    }
    
    public static IMealRecipe addPotRecipe(String mealname, boolean isDrink, int stamina, int fatigue, ICookingComponent... ingredients) {
        return addRecipe(mealname, stamina, fatigue, isDrink, ingredients).setRequiredTool(Utensil.POT);
    }
    
    public static IMealRecipe addPotRecipe(String mealname, int stamina, int fatigue, ICookingComponent... ingredients) {
        return addRecipe(mealname, stamina, fatigue, false, ingredients).setRequiredTool(Utensil.POT);
    }
    
    public static IMealRecipe addOvenRecipe(String mealname, int stamina, int fatigue, ICookingComponent... ingredients) {
        return addRecipe(mealname, stamina, fatigue, false, ingredients).setRequiredTool(Utensil.OVEN);
    }
}
