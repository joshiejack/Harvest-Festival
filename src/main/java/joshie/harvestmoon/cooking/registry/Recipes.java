package joshie.harvestmoon.cooking.registry;

import static joshie.harvestmoon.cooking.Utensil.FRYING_PAN;
import static joshie.harvestmoon.cooking.registry.Ingredients.bamboo_shoot;
import static joshie.harvestmoon.cooking.registry.Ingredients.cabbage;
import static joshie.harvestmoon.cooking.registry.Ingredients.carrot;
import static joshie.harvestmoon.cooking.registry.Ingredients.egg;
import static joshie.harvestmoon.cooking.registry.Ingredients.eggplant;
import static joshie.harvestmoon.cooking.registry.Ingredients.fish;
import static joshie.harvestmoon.cooking.registry.Ingredients.flour;
import static joshie.harvestmoon.cooking.registry.Ingredients.green_pepper;
import static joshie.harvestmoon.cooking.registry.Ingredients.matsutake_mushroom;
import static joshie.harvestmoon.cooking.registry.Ingredients.oil;
import static joshie.harvestmoon.cooking.registry.Ingredients.onion;
import static joshie.harvestmoon.cooking.registry.Ingredients.riceball;
import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.cooking.Ingredient;
import joshie.harvestmoon.cooking.Meal;
import joshie.harvestmoon.cooking.Recipe;

public class Recipes {    
    public static void init() {       
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { cabbage, oil }, new Meal("fry.stir", 40, -1, 4, 0.2F, 32)).setRequiredTool(FRYING_PAN).setOptionalIngredients(onion, carrot, bamboo_shoot, matsutake_mushroom, eggplant, green_pepper));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { riceball, oil, egg }, new Meal("rice.fried", 40, -1, 2, 0.35F, 32)).setRequiredTool(FRYING_PAN).setOptionalIngredients(carrot, onion, bamboo_shoot, green_pepper, fish));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { flour, cabbage, oil, egg }, new Meal("pancakes.savoury", 50, -1, 3, 0.3F, 24)).setRequiredTool(FRYING_PAN).setOptionalIngredients(onion));

    }
}
