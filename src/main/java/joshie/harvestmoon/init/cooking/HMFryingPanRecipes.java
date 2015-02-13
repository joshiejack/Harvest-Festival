package joshie.harvestmoon.init.cooking;

import static joshie.harvestmoon.cooking.Utensil.FRYING_PAN;
import static joshie.harvestmoon.init.cooking.HMIngredients.bamboo_shoot;
import static joshie.harvestmoon.init.cooking.HMIngredients.butter;
import static joshie.harvestmoon.init.cooking.HMIngredients.cabbage;
import static joshie.harvestmoon.init.cooking.HMIngredients.carrot;
import static joshie.harvestmoon.init.cooking.HMIngredients.corn;
import static joshie.harvestmoon.init.cooking.HMIngredients.egg;
import static joshie.harvestmoon.init.cooking.HMIngredients.eggplant;
import static joshie.harvestmoon.init.cooking.HMIngredients.fish;
import static joshie.harvestmoon.init.cooking.HMIngredients.flour;
import static joshie.harvestmoon.init.cooking.HMIngredients.green_pepper;
import static joshie.harvestmoon.init.cooking.HMIngredients.matsutake_mushroom;
import static joshie.harvestmoon.init.cooking.HMIngredients.mayonnaise;
import static joshie.harvestmoon.init.cooking.HMIngredients.milk;
import static joshie.harvestmoon.init.cooking.HMIngredients.mushroom;
import static joshie.harvestmoon.init.cooking.HMIngredients.oil;
import static joshie.harvestmoon.init.cooking.HMIngredients.onion;
import static joshie.harvestmoon.init.cooking.HMIngredients.potato_slices;
import static joshie.harvestmoon.init.cooking.HMIngredients.riceball;
import static joshie.harvestmoon.init.cooking.HMIngredients.whisked_egg;
import static joshie.harvestmoon.init.cooking.HMSeasonings.salt;
import static joshie.harvestmoon.init.cooking.HMSeasonings.sugar;
import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.cooking.Ingredient;
import joshie.harvestmoon.cooking.Meal;
import joshie.harvestmoon.cooking.Recipe;

public class HMFryingPanRecipes {
    public static void init() {
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { cabbage, oil }, new Meal("fry.stir", 40, -1, 4, 0.2F, 32)).setRequiredTool(FRYING_PAN).setOptionalIngredients(onion, carrot, bamboo_shoot, matsutake_mushroom, eggplant, green_pepper));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { riceball, oil, egg }, new Meal("rice.fried", 40, -1, 2, 0.35F, 32)).setRequiredTool(FRYING_PAN).setOptionalIngredients(carrot, onion, bamboo_shoot, green_pepper, fish));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { flour, cabbage, oil, egg }, new Meal("pancakes.savoury", 50, -1, 3, 0.3F, 24)).setRequiredTool(FRYING_PAN).setOptionalIngredients(onion));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { potato_slices, oil }, new Meal("fries.french", 15, 0, 2, 0.1F, 8)).setRequiredTool(FRYING_PAN).setOptionalSeasonings(salt));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { corn }, new Meal("popcorn", 30, -1, 2, 0.15F, 16)).setRequiredTool(FRYING_PAN).setOptionalIngredients(butter).setOptionalSeasonings(salt));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { corn, milk }, new Meal("cornflakes", 10, -2, 2, 0.1F, 16)).setRequiredTool(FRYING_PAN).setOptionalSeasonings(sugar));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { whisked_egg, oil }, new Meal("egg.scrambled", 40, -3, 2, 0.2F, 12)).setRequiredTool(FRYING_PAN).setOptionalIngredients(butter, mayonnaise).setOptionalSeasonings(salt));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { whisked_egg, oil, milk }, new Meal("omelet", 50, -4, 2, 0.3F, 16)).setRequiredTool(FRYING_PAN).setOptionalSeasonings(salt));
        FoodRegistry.addRecipe(new Recipe(new Ingredient[] { egg, milk, oil, riceball }, new Meal("omelet.rice", 60, -4, 3, 0.35F, 32)).setRequiredTool(FRYING_PAN).setOptionalIngredients(cabbage, onion, mushroom, green_pepper).setOptionalSeasonings(salt));
    }
}
