package harvestmoon.init;

import harvestmoon.cooking.registry.Ingredients;
import harvestmoon.cooking.registry.Recipes;
import harvestmoon.cooking.registry.Seasonings;

public class HMCooking {
    public static void init() {
        Ingredients.init();
        Seasonings.init();
        Recipes.init();
    }
}
