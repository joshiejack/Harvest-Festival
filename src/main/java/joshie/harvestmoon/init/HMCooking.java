package joshie.harvestmoon.init;

import joshie.harvestmoon.cooking.registry.Ingredients;
import joshie.harvestmoon.cooking.registry.Recipes;
import joshie.harvestmoon.cooking.registry.Seasonings;

public class HMCooking {
    public static void init() {
        Ingredients.init();
        Seasonings.init();
        Recipes.init();
    }
}
