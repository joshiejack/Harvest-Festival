package joshie.harvestmoon.init;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.cooking.ICookingComponent;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class HMIngredients {
    /** Categories **/
    public static ICookingComponent mushroom;
    public static ICookingComponent juice_vegetable;
    public static ICookingComponent salad_ingredient;
    public static ICookingComponent sandwich_ingredient;
    public static ICookingComponent sashimi_vegetable;
    
    /** Seasonings **/
    public static ICookingComponent salt;
    public static ICookingComponent sugar;

    /** Created **/
    public static ICookingComponent butter;
    public static ICookingComponent boiled_egg;
    public static ICookingComponent sashimi;
    public static ICookingComponent scrambled_egg;
    public static ICookingComponent cookies;

    /** Other stuff **/
    public static ICookingComponent apple;
    public static ICookingComponent chocolate;
    public static ICookingComponent egg;
    public static ICookingComponent fish;
    public static ICookingComponent flour;
    public static ICookingComponent oil;
    public static ICookingComponent riceball;
    public static ICookingComponent milk;
    public static ICookingComponent mayonnaise;

    /** Crops **/
    public static ICookingComponent turnip;
    public static ICookingComponent potato;
    public static ICookingComponent cucumber;
    public static ICookingComponent strawberry;
    public static ICookingComponent cabbage;
    public static ICookingComponent tomato;
    public static ICookingComponent onion;
    public static ICookingComponent corn;
    public static ICookingComponent pumpkin;
    public static ICookingComponent pineapple;
    public static ICookingComponent eggplant;
    public static ICookingComponent carrot;
    public static ICookingComponent sweet_potato;
    public static ICookingComponent spinach;
    public static ICookingComponent green_pepper;

    /** Vanilla foods **/
    public static ICookingComponent watermelon;
    public static ICookingComponent wheat;
    public static ICookingComponent bread;
    public static ICookingComponent red_mushroom;
    public static ICookingComponent brown_mushroom;

    public static void addIngredients() {
        //Categories
        mushroom = HMApi.COOKING.newCategory("mushroom");
        juice_vegetable = HMApi.COOKING.newCategory("vegetable_juice");
        salad_ingredient = HMApi.COOKING.newCategory("salad_ingredient");
        sandwich_ingredient = HMApi.COOKING.newCategory("sandwich_ingredient");
        sashimi_vegetable = HMApi.COOKING.newCategory("sashimi_vegetable");

        //Seasonings
        salt = HMApi.COOKING.newIngredient("salt", 0, 0, 0, 0.01F, 0);
        sugar = HMApi.COOKING.newIngredient("sugar", 1, 0, 0, 0F, 0);

        //Store Bought
        chocolate = HMApi.COOKING.newIngredient("chocolate", 5, -10, 1, 0.05F, 4);
        flour = HMApi.COOKING.newIngredient("flour", 0, 0, 0, 0.1F, 4);
        oil = HMApi.COOKING.newIngredient("oil", 0, -2, 0, 0F, 2).setFluid(HMCooking.cookingOil);
        riceball = HMApi.COOKING.newIngredient("riceball", 6, -3, 1, 0.085F, 16);

        //HM Sizeables
        egg = HMApi.COOKING.newIngredient("egg", 6, 0, 0, 0.075F, 10);
        mayonnaise = HMApi.COOKING.newIngredient("mayonnaise", 6, -1, 1, 0.8F, 8);
        milk = HMApi.COOKING.newIngredient("milk", 5, -2, 0, 0.04F, 6).setFluid(HMCooking.cookingMilk);

        //HM Crops
        turnip = HMApi.COOKING.newIngredient("turnip", 2, -1, 1, 0.033F, 8);
        potato = HMApi.COOKING.newIngredient("potato", 5, -1, 1, 0.08F, 16);
        cucumber = HMApi.COOKING.newIngredient("cucumber", 3, -1, 1, 0.02F, 4);
        strawberry = HMApi.COOKING.newIngredient("strawberry", 3, -2, 1, 0.025F, 8);
        cabbage = HMApi.COOKING.newIngredient("cabbage", 3, -3, 1, 0.05F, 8);
        tomato = HMApi.COOKING.newIngredient("tomato", 2, -1, 1, 0.035F, 10);
        onion = HMApi.COOKING.newIngredient("onion", 2, -2, 1, 0.03F, 8);
        corn = HMApi.COOKING.newIngredient("corn", 5, -3, 1, 0.0375F, 16);
        pumpkin = HMApi.COOKING.newIngredient("pumpkin", 4, -1, 2, 0.06F, 16);
        pineapple = HMApi.COOKING.newIngredient("pineapple", 3, -3, 1, 0.055F, 16);
        eggplant = HMApi.COOKING.newIngredient("eggplant", 2, -3, 1, 0.05F, 8);
        carrot = HMApi.COOKING.newIngredient("carrot", 2, -2, 1, 0.035F, 6);
        sweet_potato = HMApi.COOKING.newIngredient("potato_sweet", 6, -2, 1, 0.04F, 16);
        spinach = HMApi.COOKING.newIngredient("spinach", 3, -4, 1, 0.022F, 8);
        green_pepper = HMApi.COOKING.newIngredient("pepper_green", 4, -2, 1, 0.03F, 12);

        //Vanilla Stuff
        apple = HMApi.COOKING.newIngredient("apple", 2, -1, 1, 0.015F, 6);
        bread = HMApi.COOKING.newIngredient("bread", 8, -6, 3, 0.06F, 24);
        fish = HMApi.COOKING.newIngredient("fish", 5, -5, 2, 0.1F, 16);
        watermelon = HMApi.COOKING.newIngredient("watermelon", 1, -1, 1, 0.05F, 4);
        wheat = HMApi.COOKING.newIngredient("wheat", 6, -3, 1, 0.0275F, 16);
        red_mushroom = HMApi.COOKING.newIngredient("mushroom_red", 2, -1, 1, 0.02F, 8);
        brown_mushroom = HMApi.COOKING.newIngredient("mushroom_brown", 2, -1, 1, 0.02F, 8);

        //Meals
        butter = HMApi.COOKING.newIngredient("butter", 6, -1, 0, 0.01F, 6);
        boiled_egg = HMApi.COOKING.newIngredient("egg.boiled", 10, -1, 2, 0.06F, 8);
        sashimi = HMApi.COOKING.newIngredient("sashimi", 11, -2, 2, 0.07F, 10);
        scrambled_egg = HMApi.COOKING.newIngredient("egg.scrambled", 20, -1, 1, 0.05F, 6);
        cookies = HMApi.COOKING.newIngredient("cookies", 15, -2, 1, 0.03F, 4);
        
        //Add ingredients to the categories
        mushroom.add(red_mushroom, brown_mushroom);
        juice_vegetable.add(turnip, cucumber, cabbage, tomato, onion, carrot, spinach, green_pepper);
        salad_ingredient.add(cucumber, carrot, tomato, cabbage, brown_mushroom);
        sandwich_ingredient.add(cucumber, tomato, mayonnaise, brown_mushroom, boiled_egg);
        sashimi_vegetable.add(cucumber, tomato, onion, eggplant);
    }

    public static void assignIngredients() {
        HMApi.COOKING.register(new ItemStack(HMItems.general, 1, ItemGeneral.SALT), salt);
        HMApi.COOKING.register(new ItemStack(Items.sugar, 1, OreDictionary.WILDCARD_VALUE), sugar);

        HMApi.COOKING.register(new ItemStack(Items.apple, 1, OreDictionary.WILDCARD_VALUE), apple);
        HMApi.COOKING.register(new ItemStack(HMItems.general, 1, ItemGeneral.CHOCOLATE), chocolate);
        HMApi.COOKING.register(new ItemStack(HMItems.general, 1, ItemGeneral.FLOUR), flour);
        HMApi.COOKING.register(new ItemStack(HMItems.general, 1, ItemGeneral.OIL), oil);
        HMApi.COOKING.register(new ItemStack(HMItems.general, 1, ItemGeneral.RICEBALL), riceball);

        HMApi.COOKING.register(HMCrops.turnip.getCropStack(), turnip);
        HMApi.COOKING.register(HMCrops.potato.getCropStack(), potato);
        HMApi.COOKING.register(new ItemStack(Items.potato, 1, OreDictionary.WILDCARD_VALUE), potato);
        HMApi.COOKING.register(HMCrops.cucumber.getCropStack(), cucumber);
        HMApi.COOKING.register(HMCrops.strawberry.getCropStack(), strawberry);
        HMApi.COOKING.register(HMCrops.cabbage.getCropStack(), cabbage);
        HMApi.COOKING.register(HMCrops.tomato.getCropStack(), tomato);
        HMApi.COOKING.register(HMCrops.onion.getCropStack(), onion);
        HMApi.COOKING.register(HMCrops.corn.getCropStack(), corn);
        HMApi.COOKING.register(HMCrops.pumpkin.getCropStack(), pumpkin);
        HMApi.COOKING.register(new ItemStack(Blocks.pumpkin, 1, OreDictionary.WILDCARD_VALUE), pumpkin);
        HMApi.COOKING.register(HMCrops.pineapple.getCropStack(), pineapple);
        HMApi.COOKING.register(HMCrops.eggplant.getCropStack(), eggplant);
        HMApi.COOKING.register(HMCrops.carrot.getCropStack(), carrot);
        HMApi.COOKING.register(new ItemStack(Items.carrot, 1, OreDictionary.WILDCARD_VALUE), carrot);
        HMApi.COOKING.register(HMCrops.sweet_potato.getCropStack(), sweet_potato);
        HMApi.COOKING.register(HMCrops.spinach.getCropStack(), spinach);
        HMApi.COOKING.register(HMCrops.green_pepper.getCropStack(), green_pepper);

        HMApi.COOKING.register(new ItemStack(Items.wheat, 1, OreDictionary.WILDCARD_VALUE), wheat);
        HMApi.COOKING.register(new ItemStack(Items.melon, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        HMApi.COOKING.register(new ItemStack(Blocks.melon_block, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        HMApi.COOKING.register(new ItemStack(Items.bread, 1, OreDictionary.WILDCARD_VALUE), bread);

        HMApi.COOKING.register(new ItemStack(HMItems.egg), egg);
        HMApi.COOKING.register(new ItemStack(Items.egg, 1, OreDictionary.WILDCARD_VALUE), egg);
        HMApi.COOKING.register(new ItemStack(Items.fish, 1, OreDictionary.WILDCARD_VALUE), fish);
        HMApi.COOKING.register(new ItemStack(HMItems.milk), milk);
        HMApi.COOKING.register(new ItemStack(Items.milk_bucket, 1, OreDictionary.WILDCARD_VALUE), milk);
        HMApi.COOKING.register(new ItemStack(HMItems.mayonnaise), mayonnaise);
        
        //Meal Ingredients
        HMApi.COOKING.register(HMApi.COOKING.getMeal("butter"), butter);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("egg.boiled"), boiled_egg);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("sashimi"), sashimi);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("egg.scrambled"), scrambled_egg);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("cookies"), cookies);

        //Mushrooms
        HMApi.COOKING.register(new ItemStack(Blocks.brown_mushroom, 1, OreDictionary.WILDCARD_VALUE), brown_mushroom);
        HMApi.COOKING.register(new ItemStack(Blocks.red_mushroom, 1, OreDictionary.WILDCARD_VALUE), red_mushroom);
    }
}
