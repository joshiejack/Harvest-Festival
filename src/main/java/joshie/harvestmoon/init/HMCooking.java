package joshie.harvestmoon.init;

import joshie.harvestmoon.init.cooking.HMFryingPanRecipes;
import joshie.harvestmoon.init.cooking.HMIngredients;
import joshie.harvestmoon.init.cooking.HMSeasonings;


public class HMCooking {
    public static void init() {
        HMIngredients.init();
        HMSeasonings.init();
        HMFryingPanRecipes.init();
    }
}
