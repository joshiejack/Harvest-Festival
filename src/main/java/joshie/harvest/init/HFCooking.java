package joshie.harvest.init;

import static joshie.harvest.core.helpers.RecipeHelper.addFryingPanRecipe;
import static joshie.harvest.core.helpers.RecipeHelper.addMixerRecipe;
import static joshie.harvest.core.helpers.RecipeHelper.addNoUtensilRecipe;
import static joshie.harvest.core.helpers.RecipeHelper.addOvenRecipe;
import static joshie.harvest.core.helpers.RecipeHelper.addPotRecipe;
import static joshie.harvest.init.HFIngredients.apple;
import static joshie.harvest.init.HFIngredients.bread;
import static joshie.harvest.init.HFIngredients.butter;
import static joshie.harvest.init.HFIngredients.cabbage;
import static joshie.harvest.init.HFIngredients.carrot;
import static joshie.harvest.init.HFIngredients.chocolate;
import static joshie.harvest.init.HFIngredients.cookies;
import static joshie.harvest.init.HFIngredients.corn;
import static joshie.harvest.init.HFIngredients.cucumber;
import static joshie.harvest.init.HFIngredients.egg;
import static joshie.harvest.init.HFIngredients.eggplant;
import static joshie.harvest.init.HFIngredients.fish;
import static joshie.harvest.init.HFIngredients.flour;
import static joshie.harvest.init.HFIngredients.green_pepper;
import static joshie.harvest.init.HFIngredients.juice_vegetable;
import static joshie.harvest.init.HFIngredients.mayonnaise;
import static joshie.harvest.init.HFIngredients.milk;
import static joshie.harvest.init.HFIngredients.mushroom;
import static joshie.harvest.init.HFIngredients.oil;
import static joshie.harvest.init.HFIngredients.onion;
import static joshie.harvest.init.HFIngredients.pineapple;
import static joshie.harvest.init.HFIngredients.potato;
import static joshie.harvest.init.HFIngredients.pumpkin;
import static joshie.harvest.init.HFIngredients.riceball;
import static joshie.harvest.init.HFIngredients.salad_ingredient;
import static joshie.harvest.init.HFIngredients.salt;
import static joshie.harvest.init.HFIngredients.sandwich_ingredient;
import static joshie.harvest.init.HFIngredients.sashimi;
import static joshie.harvest.init.HFIngredients.sashimi_vegetable;
import static joshie.harvest.init.HFIngredients.scrambled_egg;
import static joshie.harvest.init.HFIngredients.spinach;
import static joshie.harvest.init.HFIngredients.strawberry;
import static joshie.harvest.init.HFIngredients.sugar;
import static joshie.harvest.init.HFIngredients.sweet_potato;
import static joshie.harvest.init.HFIngredients.tomato;
import static joshie.harvest.init.HFIngredients.turnip;
import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.MayonnaiseRecipeHandler;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;


public class HFCooking {
    public static Fluid cookingOil;
    public static Fluid cookingMilk;
    
    public static void init() {
        HFApi.COOKING.registerRecipeHandler(new MayonnaiseRecipeHandler());
        
        cookingOil = new Fluid("oil.cooking");
        FluidRegistry.registerFluid(cookingOil);
        
        cookingMilk = FluidRegistry.getFluid("milk");
        if (milk == null) {
            cookingMilk = new Fluid("milk");
            FluidRegistry.registerFluid(cookingMilk);
        }
        
        HFIngredients.addIngredients();
        
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
        addPotRecipe("egg.boiled", 20, -2, egg).setOptionalIngredients(salt);
        addPotRecipe("spinach.boiled", 20, -1, spinach);
        addPotRecipe("potato.candied", 8, -1, sweet_potato).setOptionalIngredients(sugar);
        addPotRecipe("dumplings", 25, -5, cabbage, onion, flour, oil).setOptionalIngredients(sugar);
        addPotRecipe("noodles", 40, -3, flour).setOptionalIngredients(salt);
//        addPotRecipe("noodles.curry", 60, -3, noodles, curry).setOptionalIngredients(salt);
        addPotRecipe("soup.rice", 10, -2, riceball);
        addPotRecipe("porridge", 8, -2, milk, riceball).setOptionalIngredients(sugar);
        addPotRecipe("egg.overrice", 12, -2, egg, riceball).setOptionalIngredients(salt);
        addPotRecipe("stew", 30, -1, milk, flour).setOptionalIngredients(eggplant, onion, potato, carrot, green_pepper, fish, salt);
        addPotRecipe("stew.pumpkin", 8, -1, pumpkin).setOptionalIngredients(sugar, salt);
        addPotRecipe("stew.fish", 7, -1, fish).setOptionalIngredients(salt);
//        addPotRecipe("stew.mountain", [you do the numbers plis], carrot, bamboo.shoot, shiitake).setOptionalIngredients(salt);     
        addOvenRecipe("corn.baked", 7, -1, corn).setOptionalIngredients(oil, butter, salt);
        addOvenRecipe("riceballs.toasted", 12, -1, riceball).setOptionalIngredients(sugar, salt);
        addOvenRecipe("toast", 16, -1, bread).setOptionalIngredients(butter);
        addOvenRecipe("dinnerroll", 9, -2, egg, milk, butter);
//        addOvenRecipe("bun.jam", [you do the numbers plis], egg, milk, jam);
        addOvenRecipe("doria", 25, -3, onion, butter, milk, riceball, flour);
        addOvenRecipe("cookies", 15, -5, egg, flour, butter).setOptionalIngredients(sugar);
        addOvenRecipe("cookies.chocolate", 30, -5, cookies, chocolate);
        addOvenRecipe("cake.chocolate", 70, -8, egg, flour, butter, chocolate).setOptionalIngredients(sugar, pineapple, apple, strawberry);
        
        HFIngredients.assignIngredients();
    }
}
