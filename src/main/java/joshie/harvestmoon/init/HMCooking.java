package joshie.harvestmoon.init;

import static joshie.harvestmoon.core.helpers.RecipeHelper.addFryingPanRecipe;
import static joshie.harvestmoon.core.helpers.RecipeHelper.addMixerRecipe;
import static joshie.harvestmoon.core.helpers.RecipeHelper.addNoUtensilRecipe;
import static joshie.harvestmoon.core.helpers.RecipeHelper.addOvenRecipe;
import static joshie.harvestmoon.core.helpers.RecipeHelper.addPotRecipe;
import static joshie.harvestmoon.init.HMIngredients.apple;
import static joshie.harvestmoon.init.HMIngredients.bread;
import static joshie.harvestmoon.init.HMIngredients.butter;
import static joshie.harvestmoon.init.HMIngredients.cabbage;
import static joshie.harvestmoon.init.HMIngredients.carrot;
import static joshie.harvestmoon.init.HMIngredients.chocolate;
import static joshie.harvestmoon.init.HMIngredients.cookies;
import static joshie.harvestmoon.init.HMIngredients.corn;
import static joshie.harvestmoon.init.HMIngredients.cucumber;
import static joshie.harvestmoon.init.HMIngredients.egg;
import static joshie.harvestmoon.init.HMIngredients.eggplant;
import static joshie.harvestmoon.init.HMIngredients.fish;
import static joshie.harvestmoon.init.HMIngredients.flour;
import static joshie.harvestmoon.init.HMIngredients.green_pepper;
import static joshie.harvestmoon.init.HMIngredients.juice_vegetable;
import static joshie.harvestmoon.init.HMIngredients.mayonnaise;
import static joshie.harvestmoon.init.HMIngredients.milk;
import static joshie.harvestmoon.init.HMIngredients.mushroom;
import static joshie.harvestmoon.init.HMIngredients.oil;
import static joshie.harvestmoon.init.HMIngredients.onion;
import static joshie.harvestmoon.init.HMIngredients.pineapple;
import static joshie.harvestmoon.init.HMIngredients.potato;
import static joshie.harvestmoon.init.HMIngredients.pumpkin;
import static joshie.harvestmoon.init.HMIngredients.riceball;
import static joshie.harvestmoon.init.HMIngredients.salad_ingredient;
import static joshie.harvestmoon.init.HMIngredients.salt;
import static joshie.harvestmoon.init.HMIngredients.sandwich_ingredient;
import static joshie.harvestmoon.init.HMIngredients.sashimi;
import static joshie.harvestmoon.init.HMIngredients.sashimi_vegetable;
import static joshie.harvestmoon.init.HMIngredients.scrambled_egg;
import static joshie.harvestmoon.init.HMIngredients.spinach;
import static joshie.harvestmoon.init.HMIngredients.strawberry;
import static joshie.harvestmoon.init.HMIngredients.sugar;
import static joshie.harvestmoon.init.HMIngredients.sweet_potato;
import static joshie.harvestmoon.init.HMIngredients.tomato;
import static joshie.harvestmoon.init.HMIngredients.turnip;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.cooking.MayonnaiseRecipeHandler;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;


public class HMCooking {
    public static Fluid cookingOil;
    public static Fluid cookingMilk;
    
    public static void init() {
        HMApi.COOKING.registerRecipeHandler(new MayonnaiseRecipeHandler());
        
        cookingOil = new Fluid("oil.cooking");
        FluidRegistry.registerFluid(cookingOil);
        
        cookingMilk = FluidRegistry.getFluid("milk");
        if (milk == null) {
            cookingMilk = new Fluid("milk");
            FluidRegistry.registerFluid(cookingMilk);
        }
        
        HMIngredients.addIngredients();
        
        //Recipes
        addFryingPanRecipe("pancake.savoury", 50, -1, flour, cabbage, oil, egg).setOptionalIngredients(onion);
        addFryingPanRecipe("fries.french", 15, 0, potato, oil).setOptionalIngredients(salt);
        addFryingPanRecipe("popcorn", 30, -1, corn).setOptionalIngredients(butter, salt);
        addFryingPanRecipe("cornflakes", 10, -2, corn, milk).setOptionalIngredients(sugar);
        addFryingPanRecipe("eggplant.happy", 30, -2, eggplant).setOptionalIngredients(sugar);
        addFryingPanRecipe("egg.scrambled", 40, -3, egg, oil).setOptionalIngredients(butter, mayonnaise, salt);
        addFryingPanRecipe("omelet", 50, -4, egg, oil, milk).setOptionalIngredients(salt);
        addFryingPanRecipe("omelet.rice", 60, -4, egg, milk, oil, riceball).setOptionalIngredients(cabbage, onion, mushroom, green_pepper, salt);
        addFryingPanRecipe("toast.french", 30, -2, egg, bread, oil, sugar).setOptionalIngredients(butter);
        addFryingPanRecipe("doughnut", 30, -2, egg, milk, butter, flour, oil);
        addFryingPanRecipe("fish.grilled", 30, -1, fish, oil, salt);
        addFryingPanRecipe("pancake", 20, -3, egg, milk, flour, oil).setOptionalIngredients(sugar, butter);
        addFryingPanRecipe("potsticker", 25, -1, cabbage, onion, flour, oil);
        addFryingPanRecipe("risotto", 35, -1, tomato, onion, riceball, oil);
    
        addMixerRecipe("juice.pineapple", 5, -15, pineapple).setOptionalIngredients(salt, sugar);
        addMixerRecipe("juice.tomato", 20, -20, tomato).setOptionalIngredients(salt);
        addMixerRecipe("milk.strawberry", 30, -15, strawberry, milk).setOptionalIngredients(sugar);
        addMixerRecipe("juice.vegetable", 20, -20, juice_vegetable).setOptionalIngredients(cucumber, onion, cabbage, tomato, spinach, carrot, green_pepper, turnip, salt);
        addMixerRecipe("latte.vegetable", 30, -20, juice_vegetable, milk).setOptionalIngredients(cucumber, onion, cabbage, tomato, spinach, carrot, green_pepper, turnip, salt);
        addMixerRecipe("ketchup", false, 1, 0, tomato, onion).setOptionalIngredients(salt, sugar);
        addMixerRecipe("butter", false, 1, 0, milk).setOptionalIngredients(salt);
        addMixerRecipe("fishsticks", false, 5, -1, fish).setOptionalIngredients(salt);
        
        addNoUtensilRecipe("turnip.pickled", 6, -2, turnip).setOptionalIngredients(salt);
        addNoUtensilRecipe("cucumber.pickled", 6, -2, cucumber).setOptionalIngredients(salt);
        addNoUtensilRecipe("salad", 10, -3, salad_ingredient).setOptionalIngredients(mushroom, cucumber, cabbage, tomato, carrot, salt);
        addNoUtensilRecipe("sandwich", 8, -2, bread, sandwich_ingredient).setOptionalIngredients(butter, tomato, cucumber, salt, mayonnaise, mushroom);
        addNoUtensilRecipe("sushi", 30, -5, sashimi, riceball);
        addNoUtensilRecipe("sashimi", 22, -4, fish);
        addNoUtensilRecipe("sashimi.chirashi", 50, -7, sashimi, scrambled_egg, riceball, sashimi_vegetable);
        
        addPotRecipe("milk.hot", true, 20, -10, milk).setOptionalIngredients(sugar);
        addPotRecipe("chocolate.hot", true, 10, -15, milk, chocolate).setOptionalIngredients(sugar);
        addPotRecipe("stew.pumpkin", 8, -1, pumpkin).setOptionalIngredients(sugar, salt);
        addPotRecipe("stew.fish", 7, -1, fish).setOptionalIngredients(salt);
        addPotRecipe("egg.boiled", 20, -2, egg).setOptionalIngredients(salt);
        addPotRecipe("spinach.boiled", 20, -1, spinach);
        addPotRecipe("potato.candied", 8, -1, sweet_potato).setOptionalIngredients(sugar);
        addPotRecipe("dumplings", 25, -5, cabbage, onion, flour, oil).setOptionalIngredients(sugar);
        addPotRecipe("noodles", 40, -3, flour).setOptionalIngredients(salt);
        addPotRecipe("soup.rice", 10, -2, riceball);
        addPotRecipe("porridge", 8, -2, milk, riceball).setOptionalIngredients(sugar);
        addPotRecipe("egg.overrice", 12, -2, egg, riceball).setOptionalIngredients(salt);
        addPotRecipe("stew", 30, -1, milk, flour).setOptionalIngredients(eggplant, onion, potato, carrot, green_pepper, fish, salt);
        
        addOvenRecipe("corn.baked", 7, -1, corn).setOptionalIngredients(oil, butter, salt);
        addOvenRecipe("riceballs.toasted", 12, -1, riceball).setOptionalIngredients(sugar, salt);
        addOvenRecipe("toast", 16, -1, bread).setOptionalIngredients(butter);
        addOvenRecipe("dinnerroll", 9, -2, egg, milk, butter);
        addOvenRecipe("sweetroll", 11, -2, egg, milk, butter, sugar);
        addOvenRecipe("doria", 25, -3, onion, butter, milk, riceball, flour);
        addOvenRecipe("cookies", 15, -5, egg, flour, butter).setOptionalIngredients(sugar);
        addOvenRecipe("cookies.chocolate", 30, -5, cookies, chocolate);
        addOvenRecipe("cake.chocolate", 70, -8, egg, flour, butter, chocolate).setOptionalIngredients(sugar, pineapple, apple, strawberry);
        
        HMIngredients.assignIngredients();
    }
}
