package joshie.harvestmoon.init.cooking;

import static joshie.harvestmoon.cooking.Utensil.FRYING_PAN;
import static joshie.harvestmoon.init.cooking.HMIngredients.bamboo_shoot;
import static joshie.harvestmoon.init.cooking.HMIngredients.cabbage;
import static joshie.harvestmoon.init.cooking.HMIngredients.carrot;
import static joshie.harvestmoon.init.cooking.HMIngredients.egg;
import static joshie.harvestmoon.init.cooking.HMIngredients.eggplant;
import static joshie.harvestmoon.init.cooking.HMIngredients.fish;
import static joshie.harvestmoon.init.cooking.HMIngredients.flour;
import static joshie.harvestmoon.init.cooking.HMIngredients.green_pepper;
import static joshie.harvestmoon.init.cooking.HMIngredients.matsutake_mushroom;
import static joshie.harvestmoon.init.cooking.HMIngredients.oil;
import static joshie.harvestmoon.init.cooking.HMIngredients.onion;
import static joshie.harvestmoon.init.cooking.HMIngredients.riceball;
import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.cooking.Ingredient;
import joshie.harvestmoon.cooking.Meal;
import joshie.harvestmoon.cooking.Recipe;

public class HMFryingPanRecipes {    
    public static void init() {       
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { cabbage, oil }, new Meal("fry.stir", 40, -1, 4, 0.2F, 32)).setRequiredTool(FRYING_PAN).setOptionalIngredients(onion, carrot, bamboo_shoot, matsutake_mushroom, eggplant, green_pepper));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { riceball, oil, egg }, new Meal("rice.fried", 40, -1, 2, 0.35F, 32)).setRequiredTool(FRYING_PAN).setOptionalIngredients(carrot, onion, bamboo_shoot, green_pepper, fish));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { flour, cabbage, oil, egg }, new Meal("pancakes.savoury", 50, -1, 3, 0.3F, 24)).setRequiredTool(FRYING_PAN).setOptionalIngredients(onion));

        //Test Rectipe
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { carrot, fish }, new Meal("test.recipe", 100, -3, 3, 0.15F, 32)).setRequiredTool(FRYING_PAN).setOptionalIngredients(egg));
    }
}
