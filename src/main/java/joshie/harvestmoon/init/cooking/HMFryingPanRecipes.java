package joshie.harvestmoon.init.cooking;

import static joshie.harvestmoon.init.cooking.HMIngredients.bread;
import static joshie.harvestmoon.init.cooking.HMIngredients.butter;
import static joshie.harvestmoon.init.cooking.HMIngredients.cabbage;
import static joshie.harvestmoon.init.cooking.HMIngredients.corn;
import static joshie.harvestmoon.init.cooking.HMIngredients.egg;
import static joshie.harvestmoon.init.cooking.HMIngredients.eggplant;
import static joshie.harvestmoon.init.cooking.HMIngredients.fish;
import static joshie.harvestmoon.init.cooking.HMIngredients.flour;
import static joshie.harvestmoon.init.cooking.HMIngredients.green_pepper;
import static joshie.harvestmoon.init.cooking.HMIngredients.mayonnaise;
import static joshie.harvestmoon.init.cooking.HMIngredients.milk;
import static joshie.harvestmoon.init.cooking.HMIngredients.mushroom;
import static joshie.harvestmoon.init.cooking.HMIngredients.oil;
import static joshie.harvestmoon.init.cooking.HMIngredients.onion;
import static joshie.harvestmoon.init.cooking.HMIngredients.potato_slices;
import static joshie.harvestmoon.init.cooking.HMIngredients.riceball;
import static joshie.harvestmoon.init.cooking.HMIngredients.salt;
import static joshie.harvestmoon.init.cooking.HMIngredients.sugar;
import static joshie.harvestmoon.init.cooking.HMIngredients.whisked_egg;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.cooking.ICookingComponent;
import joshie.harvestmoon.cooking.Meal;
import joshie.harvestmoon.cooking.Recipe.FryingPanRecipe;

public class HMFryingPanRecipes {
    public static void init() {
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { flour, cabbage, oil, egg }, new Meal("pancakes.savoury", 50, -1, 3, 0.3F, 24)).setOptionalIngredients(onion));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { potato_slices, oil }, new Meal("fries.french", 15, 0, 2, 0.1F, 8)).setOptionalIngredients(salt));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { corn }, new Meal("popcorn", 30, -1, 2, 0.15F, 16)).setOptionalIngredients(butter).setOptionalIngredients(salt));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { corn, milk }, new Meal("cornflakes", 10, -2, 2, 0.1F, 16)).setOptionalIngredients(sugar));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { eggplant }, new Meal("eggplant.happy", 30, -2, 3, 0.1F, 12)).setOptionalIngredients(sugar));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { whisked_egg, oil }, new Meal("egg.scrambled", 40, -3, 2, 0.2F, 12)).setOptionalIngredients(butter, mayonnaise, salt));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { whisked_egg, oil, milk }, new Meal("omelet", 50, -4, 2, 0.3F, 16)).setOptionalIngredients(salt));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { egg, milk, oil, riceball }, new Meal("omelet.rice", 60, -4, 3, 0.35F, 32)).setOptionalIngredients(cabbage, onion, mushroom, green_pepper, salt));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { egg, bread, oil, sugar }, new Meal("toast.french", 30, -2, 4, 0.35F, 20)).setOptionalIngredients(butter));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { egg, milk, butter, flour, oil }, new Meal("doughnut", 30, -2, 3, 0.4F, 10)));
        HMApi.COOKING.addRecipe(new FryingPanRecipe(new ICookingComponent[] { fish, oil, salt }, new Meal("fish.grilled", 30, -1, 2, 0.2F, 16)));
    }
}
