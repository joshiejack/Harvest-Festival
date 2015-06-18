package joshie.harvest.cooking;

import static joshie.harvest.cooking.HFIngredients.apple;
import static joshie.harvest.cooking.HFIngredients.bread;
import static joshie.harvest.cooking.HFIngredients.butter;
import static joshie.harvest.cooking.HFIngredients.cabbage;
import static joshie.harvest.cooking.HFIngredients.carrot;
import static joshie.harvest.cooking.HFIngredients.chocolate;
import static joshie.harvest.cooking.HFIngredients.cookies;
import static joshie.harvest.cooking.HFIngredients.corn;
import static joshie.harvest.cooking.HFIngredients.cucumber;
import static joshie.harvest.cooking.HFIngredients.egg;
import static joshie.harvest.cooking.HFIngredients.eggplant;
import static joshie.harvest.cooking.HFIngredients.fish;
import static joshie.harvest.cooking.HFIngredients.flour;
import static joshie.harvest.cooking.HFIngredients.green_pepper;
import static joshie.harvest.cooking.HFIngredients.juice_vegetable;
import static joshie.harvest.cooking.HFIngredients.mayonnaise;
import static joshie.harvest.cooking.HFIngredients.milk;
import static joshie.harvest.cooking.HFIngredients.mushroom;
import static joshie.harvest.cooking.HFIngredients.oil;
import static joshie.harvest.cooking.HFIngredients.onion;
import static joshie.harvest.cooking.HFIngredients.pineapple;
import static joshie.harvest.cooking.HFIngredients.potato;
import static joshie.harvest.cooking.HFIngredients.pumpkin;
import static joshie.harvest.cooking.HFIngredients.riceball;
import static joshie.harvest.cooking.HFIngredients.salad_ingredient;
import static joshie.harvest.cooking.HFIngredients.salt;
import static joshie.harvest.cooking.HFIngredients.sandwich_ingredient;
import static joshie.harvest.cooking.HFIngredients.sashimi;
import static joshie.harvest.cooking.HFIngredients.sashimi_vegetable;
import static joshie.harvest.cooking.HFIngredients.scrambled_egg;
import static joshie.harvest.cooking.HFIngredients.spinach;
import static joshie.harvest.cooking.HFIngredients.strawberry;
import static joshie.harvest.cooking.HFIngredients.sugar;
import static joshie.harvest.cooking.HFIngredients.sweet_potato;
import static joshie.harvest.cooking.HFIngredients.tomato;
import static joshie.harvest.cooking.HFIngredients.turnip;
import static joshie.harvest.core.helpers.RecipeHelper.addFryingPanRecipe;
import static joshie.harvest.core.helpers.RecipeHelper.addMixerRecipe;
import static joshie.harvest.core.helpers.RecipeHelper.addNoUtensilRecipe;
import static joshie.harvest.core.helpers.RecipeHelper.addOvenRecipe;
import static joshie.harvest.core.helpers.RecipeHelper.addPotRecipe;

public class HFRecipes {
    public static void preInit() {
      //Recipes
        //Frying Pan
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

        //Mixer
        addMixerRecipe("juice.pineapple", 5, -15, pineapple).setOptionalIngredients(salt, sugar);
        addMixerRecipe("juice.tomato", 20, -20, tomato).setOptionalIngredients(salt);
        addMixerRecipe("milk.strawberry", 30, -15, strawberry, milk).setOptionalIngredients(sugar);
        addMixerRecipe("juice.vegetable", 20, -20, juice_vegetable).setOptionalIngredients(cucumber, onion, cabbage, tomato, spinach, carrot, green_pepper, turnip, salt); //Yo this doesnt make any sense. It requires Vegetable Juice to make iteslf?
        addMixerRecipe("latte.vegetable", 30, -20, juice_vegetable, milk).setOptionalIngredients(cucumber, onion, cabbage, tomato, spinach, carrot, green_pepper, turnip, salt);
        addMixerRecipe("ketchup", 1, 0, tomato, onion).setOptionalIngredients(salt, sugar);
        addMixerRecipe("butter", false, 1, 0, milk).setOptionalIngredients(salt).setHasAltTexture();
        addMixerRecipe("fishsticks", false, 5, -1, fish).setOptionalIngredients(salt);

        //Hand
        addNoUtensilRecipe("turnip.pickled", 6, -2, turnip).setOptionalIngredients(salt);
        addNoUtensilRecipe("cucumber.pickled", 6, -2, cucumber).setOptionalIngredients(salt);
        addNoUtensilRecipe("salad", 10, -3, salad_ingredient).setOptionalIngredients(mushroom, cucumber, cabbage, tomato, carrot, salt);
        addNoUtensilRecipe("sandwich", 8, -2, bread, sandwich_ingredient).setOptionalIngredients(butter, tomato, cucumber, salt, mayonnaise, mushroom);
        addNoUtensilRecipe("sushi", 30, -5, sashimi, riceball);
        addNoUtensilRecipe("sashimi", 22, -4, fish);
        addNoUtensilRecipe("sashimi.chirashi", 50, -7, sashimi, scrambled_egg, riceball, sashimi_vegetable);

        //Pot
        addPotRecipe("milk.hot", true, 20, -10, milk).setOptionalIngredients(sugar);
        addPotRecipe("chocolate.hot", true, 10, -15, milk, chocolate).setOptionalIngredients(sugar);
        addPotRecipe("egg.boiled", 20, -2, egg).setOptionalIngredients(salt);
        addPotRecipe("spinach.boiled", 20, -1, spinach);
        addPotRecipe("potato.candied", 8, -1, sweet_potato).setOptionalIngredients(sugar);
        addPotRecipe("dumplings", true, 25, -5, cabbage, onion, flour, oil).setOptionalIngredients(sugar);
        addPotRecipe("noodles", 40, -3, flour).setOptionalIngredients(salt);
        //      addPotRecipe("noodles.curry", 60, -3, noodles, curry).setOptionalIngredients(salt); //Ingredients missing
        addPotRecipe("soup.rice", 10, -2, riceball);
        addPotRecipe("porridge", 8, -2, milk, riceball).setOptionalIngredients(sugar);
        addPotRecipe("egg.overrice", 12, -2, egg, riceball).setOptionalIngredients(salt);
        addPotRecipe("stew", 30, -1, milk, flour).setOptionalIngredients(eggplant, onion, potato, carrot, green_pepper, fish, salt);
        addPotRecipe("stew.pumpkin", true, 8, -1, pumpkin).setOptionalIngredients(sugar, salt);
        addPotRecipe("stew.fish", 7, -1, fish).setOptionalIngredients(salt);
        //      addPotRecipe("stew.mountain", true, 0, 0, carrot, bamboo_shoot, shiitake).setOptionalIngredients(salt); //Numbers missing

        //Oven
        addOvenRecipe("corn.baked", 7, -1, corn).setOptionalIngredients(oil, butter, salt);
        addOvenRecipe("riceballs.toasted", 12, -1, riceball).setOptionalIngredients(sugar, salt);
        addOvenRecipe("toast", 16, -1, bread).setOptionalIngredients(butter);
        addOvenRecipe("dinnerroll", 9, -2, egg, milk, butter);
        //      addOvenRecipe("bun.jam", 0, 0, egg, milk, jam); //Numbers missing
        addOvenRecipe("doria", 25, -3, onion, butter, milk, riceball, flour);
        addOvenRecipe("cookies", 15, -5, egg, flour, butter).setOptionalIngredients(sugar);
        addOvenRecipe("cookies.chocolate", 30, -5, cookies, chocolate);
        //      addOvenRecipe("cake", 46, -11, egg, flour, butter, fruit_ingredient).setOptionalIngredients(egg, flour, butter, orange, pineapple, strawberry, peach, grape_wild); //Ingredients missing
        addOvenRecipe("cake.chocolate", 70, -8, egg, flour, butter, chocolate).setOptionalIngredients(sugar, pineapple, apple, strawberry);
        //      addOvenRecipe("cake.rice", 0, 0, festival); //Numbers missing //Can only be gotten via the New Year's Festival, not sure how you make a recipe with no ingredients.. lol if that even works 
        //      addOvenRecipe("cake.rice.toasted", 0, 0, cake_rice); //Numbers missing
        //      addOvenRecipe("cake.cheese", 50, -5, egg, cheese, milk);
        //      addOvenRecipe("cake.apple", 0, 0, apple, egg, butter, flour); //Numbers missing
    }
}
