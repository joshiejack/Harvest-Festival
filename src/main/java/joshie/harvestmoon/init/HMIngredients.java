package joshie.harvestmoon.init;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.cooking.ICookingComponent;
import joshie.harvestmoon.cooking.Ingredient;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class HMIngredients {
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
    public static ICookingComponent mushroom;
    public static ICookingComponent juice_vegetable;
    public static ICookingComponent salad_ingredient;
    public static ICookingComponent sandwich_ingredient;
    public static ICookingComponent sashimi_vegetable;

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

    public static void addIngredients() {
        mushroom = new Ingredient("mushroom", 2, -1, 1, 0.02F, 8);
        juice_vegetable = new Ingredient("vegetable_juice", 3, -2, 1, 0.01F, 4);
        salad_ingredient = new Ingredient("salad_ingredient", 3, -3, 2, 0.05F, 8);
        sandwich_ingredient = new Ingredient("sandwich_ingredient", 2, -4, 1, 0.075F, 6);
        sashimi_vegetable = new Ingredient("sashimi_vegetable", 2, -2, 1, 0.1F, 8);

        salt = new Ingredient("salt", 0, 0, 0, 0.01F, 0);
        sugar = new Ingredient("sugar", 1, 0, 0, 0F, 0);

        butter = new Ingredient("butter", 6, -1, 0, 0.01F, 6);

        apple = new Ingredient("apple", 2, -1, 1, 0.015F, 6);
        chocolate = new Ingredient("chocolate", 5, -10, 1, 0.05F, 4);
        egg = new Ingredient("egg", 6, 0, 0, 0.075F, 10);
        fish = new Ingredient("fish", 5, -5, 2, 0.1F, 16);
        flour = new Ingredient("flour", 0, 0, 0, 0.1F, 4);
        oil = new Ingredient("oil", 0, -2, 0, 0F, 2).setFluid(HMCooking.cookingOil);
        riceball = new Ingredient("riceball", 6, -3, 1, 0.085F, 16);
        mayonnaise = new Ingredient("mayonnaise", 6, -1, 1, 0.8F, 8);
        milk = new Ingredient("milk", 5, -2, 0, 0.04F, 6).setFluid(HMCooking.cookingMilk);

        //HM Crops
        turnip = new Ingredient("turnip", 2, -1, 1, 0.033F, 8);
        potato = new Ingredient("potato", 5, -1, 1, 0.08F, 16);
        cucumber = new Ingredient("cucumber", 3, -1, 1, 0.02F, 4);
        strawberry = new Ingredient("strawberry", 3, -2, 1, 0.025F, 8);
        cabbage = new Ingredient("cabbage", 3, -3, 1, 0.05F, 8);
        tomato = new Ingredient("tomato", 2, -1, 1, 0.035F, 10);
        onion = new Ingredient("onion", 2, -2, 1, 0.03F, 8);
        corn = new Ingredient("corn", 5, -3, 1, 0.0375F, 16);
        pumpkin = new Ingredient("pumpkin", 4, -1, 2, 0.06F, 16);
        pineapple = new Ingredient("pineapple", 3, -3, 1, 0.055F, 16);
        eggplant = new Ingredient("eggplant", 2, -3, 1, 0.05F, 8);
        carrot = new Ingredient("carrot", 2, -2, 1, 0.035F, 6);
        sweet_potato = new Ingredient("potato_sweet", 6, -2, 1, 0.04F, 16);
        spinach = new Ingredient("spinach", 3, -4, 1, 0.022F, 8);
        green_pepper = new Ingredient("pepper_green", 4, -2, 1, 0.03F, 12);

        //Vanilla Crops
        watermelon = new Ingredient("watermelon", 1, -1, 1, 0.05F, 4);
        wheat = new Ingredient("wheat", 6, -3, 1, 0.0275F, 16);
        bread = new Ingredient("bread", 8, -6, 3, 0.06F, 24);

        //Meals
        boiled_egg = new Ingredient("egg.boiled", 10, -1, 2, 0.06F, 8);
        sashimi = new Ingredient("sashimi", 11, -2, 2, 0.07F, 10);
        scrambled_egg = new Ingredient("egg.scrambled", 20, -1, 1, 0.05F, 6);
        cookies = new Ingredient("cookies", 15, -2, 1, 0.03F, 4);
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

        //Mushrooms
        HMApi.COOKING.register(new ItemStack(Blocks.brown_mushroom, 1, OreDictionary.WILDCARD_VALUE), mushroom);
        HMApi.COOKING.register(new ItemStack(Blocks.red_mushroom, 1, OreDictionary.WILDCARD_VALUE), mushroom);
        //Juice Vegetables
        HMApi.COOKING.register(HMCrops.turnip.getCropStack(), juice_vegetable);
        HMApi.COOKING.register(HMCrops.cucumber.getCropStack(), juice_vegetable);
        HMApi.COOKING.register(HMCrops.cabbage.getCropStack(), juice_vegetable);
        HMApi.COOKING.register(HMCrops.tomato.getCropStack(), juice_vegetable);
        HMApi.COOKING.register(HMCrops.onion.getCropStack(), juice_vegetable);
        HMApi.COOKING.register(HMCrops.carrot.getCropStack(), juice_vegetable);
        HMApi.COOKING.register(new ItemStack(Items.carrot, 1, OreDictionary.WILDCARD_VALUE), juice_vegetable);
        HMApi.COOKING.register(HMCrops.spinach.getCropStack(), juice_vegetable);
        HMApi.COOKING.register(HMCrops.green_pepper.getCropStack(), juice_vegetable);
        //Salad Ingredients
        HMApi.COOKING.register(HMCrops.cucumber.getCropStack(), salad_ingredient);
        HMApi.COOKING.register(HMCrops.carrot.getCropStack(), salad_ingredient);
        HMApi.COOKING.register(new ItemStack(Items.carrot, 1, OreDictionary.WILDCARD_VALUE), salad_ingredient);
        HMApi.COOKING.register(HMCrops.tomato.getCropStack(), salad_ingredient);
        HMApi.COOKING.register(HMCrops.cabbage.getCropStack(), salad_ingredient);
        HMApi.COOKING.register(new ItemStack(Blocks.brown_mushroom, 1, OreDictionary.WILDCARD_VALUE), salad_ingredient);
        HMApi.COOKING.register(new ItemStack(Blocks.red_mushroom, 1, OreDictionary.WILDCARD_VALUE), salad_ingredient);
        //Sandwich Ingredients
        HMApi.COOKING.register(HMCrops.cucumber.getCropStack(), sandwich_ingredient);
        HMApi.COOKING.register(HMCrops.tomato.getCropStack(), sandwich_ingredient);
        HMApi.COOKING.register(new ItemStack(HMItems.mayonnaise), sandwich_ingredient);
        HMApi.COOKING.register(new ItemStack(Blocks.brown_mushroom, 1, OreDictionary.WILDCARD_VALUE), sandwich_ingredient);
        HMApi.COOKING.register(new ItemStack(Blocks.red_mushroom, 1, OreDictionary.WILDCARD_VALUE), sandwich_ingredient);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("egg.boiled"), sandwich_ingredient);
        //Sashimi Veggies
        HMApi.COOKING.register(HMCrops.cucumber.getCropStack(), sashimi_vegetable);
        HMApi.COOKING.register(HMCrops.tomato.getCropStack(), sashimi_vegetable);
        HMApi.COOKING.register(HMCrops.onion.getCropStack(), sashimi_vegetable);
        HMApi.COOKING.register(HMCrops.eggplant.getCropStack(), sashimi_vegetable);

        //Meal Ingredients
        HMApi.COOKING.register(HMApi.COOKING.getMeal("butter"), butter);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("egg.boiled"), boiled_egg);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("sashimi"), sashimi);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("egg.scrambled"), scrambled_egg);
        HMApi.COOKING.register(HMApi.COOKING.getMeal("cookies"), cookies);
    }
}
