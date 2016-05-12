package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.cooking.render.MappingEvent;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.items.HFItems;
import joshie.harvest.items.ItemGeneral;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class HFIngredients {
    /**
     * Categories
     **/
    public static ICookingIngredient mushroom;
    public static ICookingIngredient juice_vegetable;
    public static ICookingIngredient salad_ingredient;
    public static ICookingIngredient sandwich_ingredient;
    public static ICookingIngredient sashimi_vegetable;

    /**
     * Seasonings
     **/
    public static ICookingIngredient salt;
    public static ICookingIngredient sugar;

    /**
     * Created
     **/
    public static ICookingIngredient butter;
    public static ICookingIngredient boiled_egg;
    public static ICookingIngredient sashimi;
    public static ICookingIngredient scrambled_egg;
    public static ICookingIngredient cookies;

    /**
     * Other stuff
     **/
    public static ICookingIngredient apple;
    public static ICookingIngredient chocolate;
    public static ICookingIngredient egg;
    public static ICookingIngredient fish;
    public static ICookingIngredient flour;
    public static ICookingIngredient oil;
    public static ICookingIngredient riceball;
    public static ICookingIngredient milk;
    public static ICookingIngredient mayonnaise;
    public static ICookingIngredient ketchup;

    /**
     * Crops
     **/
    public static ICookingIngredient turnip;
    public static ICookingIngredient potato;
    public static ICookingIngredient cucumber;
    public static ICookingIngredient strawberry;
    public static ICookingIngredient cabbage;
    public static ICookingIngredient tomato;
    public static ICookingIngredient onion;
    public static ICookingIngredient corn;
    public static ICookingIngredient pumpkin;
    public static ICookingIngredient pineapple;
    public static ICookingIngredient eggplant;
    public static ICookingIngredient carrot;
    public static ICookingIngredient sweet_potato;
    public static ICookingIngredient spinach;
    public static ICookingIngredient green_pepper;

    /**
     * Vanilla foods
     **/
    public static ICookingIngredient watermelon;
    public static ICookingIngredient wheat;
    public static ICookingIngredient bread;
    public static ICookingIngredient red_mushroom;
    public static ICookingIngredient brown_mushroom;

    public static void preInit() {
        //Categories
        mushroom = HFApi.COOKING.newCategory("mushroom");
        juice_vegetable = HFApi.COOKING.newCategory("vegetable_juice");
        salad_ingredient = HFApi.COOKING.newCategory("salad_ingredient");
        sandwich_ingredient = HFApi.COOKING.newCategory("sandwich_ingredient");
        sashimi_vegetable = HFApi.COOKING.newCategory("sashimi_vegetable");

        //Seasonings
        salt = HFApi.COOKING.newIngredient("salt", 0, 0, 0, 0.01F, 0);
        sugar = HFApi.COOKING.newIngredient("sugar", 1, 0, 0, 0F, 0);

        //Store Bought
        chocolate = HFApi.COOKING.newIngredient("chocolate", 5, -10, 1, 0.05F, 4);
        flour = HFApi.COOKING.newIngredient("flour", 0, 0, 0, 0.1F, 4);
        oil = HFApi.COOKING.newIngredient("oil", 0, -2, 0, 0F, 2).setFluid(MappingEvent.OIL);
        riceball = HFApi.COOKING.newIngredient("riceball", 6, -3, 1, 0.085F, 16);

        //Sizeables
        egg = HFApi.COOKING.newIngredient("egg", 6, 0, 0, 0.075F, 10);
        mayonnaise = HFApi.COOKING.newIngredient("mayonnaise", 6, -1, 1, 0.8F, 8);
        milk = HFApi.COOKING.newIngredient("milk", 5, -2, 0, 0.04F, 6).setFluid(MappingEvent.MILK);

        //Crops
        turnip = HFApi.COOKING.newIngredient("turnip", 2, -1, 1, 0.033F, 8);
        potato = HFApi.COOKING.newIngredient("potato", 5, -1, 1, 0.08F, 16);
        cucumber = HFApi.COOKING.newIngredient("cucumber", 3, -1, 1, 0.02F, 4);
        strawberry = HFApi.COOKING.newIngredient("strawberry", 3, -2, 1, 0.025F, 8);
        cabbage = HFApi.COOKING.newIngredient("cabbage", 3, -3, 1, 0.05F, 8);
        tomato = HFApi.COOKING.newIngredient("tomato", 2, -1, 1, 0.035F, 10);
        onion = HFApi.COOKING.newIngredient("onion", 2, -2, 1, 0.03F, 8);
        corn = HFApi.COOKING.newIngredient("corn", 5, -3, 1, 0.0375F, 16);
        pumpkin = HFApi.COOKING.newIngredient("pumpkin", 4, -1, 2, 0.06F, 16);
        pineapple = HFApi.COOKING.newIngredient("pineapple", 3, -3, 1, 0.055F, 16);
        eggplant = HFApi.COOKING.newIngredient("eggplant", 2, -3, 1, 0.05F, 8);
        carrot = HFApi.COOKING.newIngredient("carrot", 2, -2, 1, 0.035F, 6);
        sweet_potato = HFApi.COOKING.newIngredient("potato_sweet", 6, -2, 1, 0.04F, 16);
        spinach = HFApi.COOKING.newIngredient("spinach", 3, -4, 1, 0.022F, 8);
        green_pepper = HFApi.COOKING.newIngredient("pepper_green", 4, -2, 1, 0.03F, 12);

        //Vanilla Stuff
        apple = HFApi.COOKING.newIngredient("apple", 2, -1, 1, 0.015F, 6);
        bread = HFApi.COOKING.newIngredient("bread", 8, -6, 3, 0.06F, 24);
        fish = HFApi.COOKING.newIngredient("fish", 5, -5, 2, 0.1F, 16);
        watermelon = HFApi.COOKING.newIngredient("watermelon", 1, -1, 1, 0.05F, 4);
        wheat = HFApi.COOKING.newIngredient("wheat", 6, -3, 1, 0.0275F, 16);
        red_mushroom = HFApi.COOKING.newIngredient("mushroom_red", 2, -1, 1, 0.02F, 8);
        brown_mushroom = HFApi.COOKING.newIngredient("mushroom_brown", 2, -1, 1, 0.02F, 8);

        //Meals
        butter = HFApi.COOKING.newIngredient("butter", 6, -1, 0, 0.01F, 6);
        boiled_egg = HFApi.COOKING.newIngredient("egg.boiled", 10, -1, 2, 0.06F, 8);
        sashimi = HFApi.COOKING.newIngredient("sashimi", 11, -2, 2, 0.07F, 10);
        scrambled_egg = HFApi.COOKING.newIngredient("egg.scrambled", 20, -1, 1, 0.05F, 6);
        cookies = HFApi.COOKING.newIngredient("cookies", 15, -2, 1, 0.03F, 4);
        //Idk if the numbers are right, check it yoshie
        ketchup = HFApi.COOKING.newIngredient("ketchup", 2, -1, 1, 0.033F, 8);

        //Add ingredients to the categories
        mushroom.add(red_mushroom, brown_mushroom);
        juice_vegetable.add(turnip, cucumber, cabbage, tomato, onion, carrot, spinach, green_pepper);
        salad_ingredient.add(cucumber, carrot, tomato, cabbage, brown_mushroom);
        sandwich_ingredient.add(cucumber, tomato, mayonnaise, brown_mushroom, boiled_egg);
        sashimi_vegetable.add(cucumber, tomato, onion, eggplant);
    }

    public static void init() {
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.SALT), salt);
        HFApi.COOKING.register(new ItemStack(Items.SUGAR, 1, OreDictionary.WILDCARD_VALUE), sugar);
        HFApi.COOKING.register(new ItemStack(Items.APPLE, 1, OreDictionary.WILDCARD_VALUE), apple);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.CHOCOLATE), chocolate);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.FLOUR), flour);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.OIL), oil);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.RICEBALL), riceball);
        HFApi.COOKING.register(HFCrops.turnip.getCropStack(), turnip);
        HFApi.COOKING.register(HFCrops.potato.getCropStack(), potato);
        HFApi.COOKING.register(new ItemStack(Items.POTATO, 1, OreDictionary.WILDCARD_VALUE), potato);
        HFApi.COOKING.register(HFCrops.cucumber.getCropStack(), cucumber);
        HFApi.COOKING.register(HFCrops.strawberry.getCropStack(), strawberry);
        HFApi.COOKING.register(HFCrops.cabbage.getCropStack(), cabbage);
        HFApi.COOKING.register(HFCrops.tomato.getCropStack(), tomato);
        HFApi.COOKING.register(HFCrops.onion.getCropStack(), onion);
        HFApi.COOKING.register(HFCrops.corn.getCropStack(), corn);
        HFApi.COOKING.register(HFCrops.pumpkin.getCropStack(), pumpkin);
        HFApi.COOKING.register(new ItemStack(Blocks.PUMPKIN, 1, OreDictionary.WILDCARD_VALUE), pumpkin);
        HFApi.COOKING.register(HFCrops.pineapple.getCropStack(), pineapple);
        HFApi.COOKING.register(HFCrops.eggplant.getCropStack(), eggplant);
        HFApi.COOKING.register(HFCrops.carrot.getCropStack(), carrot);
        HFApi.COOKING.register(new ItemStack(Items.CARROT, 1, OreDictionary.WILDCARD_VALUE), carrot);
        HFApi.COOKING.register(HFCrops.sweet_potato.getCropStack(), sweet_potato);
        HFApi.COOKING.register(HFCrops.spinach.getCropStack(), spinach);
        HFApi.COOKING.register(HFCrops.green_pepper.getCropStack(), green_pepper);
        HFApi.COOKING.register(new ItemStack(Items.WHEAT, 1, OreDictionary.WILDCARD_VALUE), wheat);
        HFApi.COOKING.register(new ItemStack(Items.MELON, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        HFApi.COOKING.register(new ItemStack(Blocks.MELON_BLOCK, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        HFApi.COOKING.register(new ItemStack(Items.BREAD, 1, OreDictionary.WILDCARD_VALUE), bread);
        HFApi.COOKING.register(new ItemStack(HFItems.EGG), egg);
        HFApi.COOKING.register(new ItemStack(Items.EGG, 1, OreDictionary.WILDCARD_VALUE), egg);
        HFApi.COOKING.register(new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE), fish);
        HFApi.COOKING.register(new ItemStack(HFItems.MILK), milk);
        HFApi.COOKING.register(new ItemStack(Items.MILK_BUCKET, 1, OreDictionary.WILDCARD_VALUE), milk);
        HFApi.COOKING.register(new ItemStack(HFItems.MAYONNAISE), mayonnaise);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.BUTTER), butter);
        HFApi.COOKING.register(HFApi.COOKING.getBestMeal("butter"), butter);
        HFApi.COOKING.register(HFApi.COOKING.getBestMeal("egg.boiled"), boiled_egg);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.SASHIMI), sashimi);
        HFApi.COOKING.register(HFApi.COOKING.getBestMeal("sashimi"), sashimi);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.EGG_SCRAMBLED), scrambled_egg);
        HFApi.COOKING.register(HFApi.COOKING.getBestMeal("egg.scrambled"), scrambled_egg);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.COOKIES), cookies);
        HFApi.COOKING.register(HFApi.COOKING.getBestMeal("cookies"), cookies);
        HFApi.COOKING.register(new ItemStack(HFItems.GENERAL, 1, ItemGeneral.KETCHUP), ketchup);
        HFApi.COOKING.register(HFApi.COOKING.getBestMeal("ketchup"), ketchup);
        HFApi.COOKING.register(new ItemStack(Blocks.BROWN_MUSHROOM, 1, OreDictionary.WILDCARD_VALUE), brown_mushroom);
        HFApi.COOKING.register(new ItemStack(Blocks.RED_MUSHROOM, 1, OreDictionary.WILDCARD_VALUE), red_mushroom);
    }
}