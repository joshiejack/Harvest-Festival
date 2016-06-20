package joshie.harvest.cooking;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.cooking.render.MappingEvent;
import joshie.harvest.crops.HFCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static joshie.harvest.cooking.items.ItemIngredients.Ingredient.*;

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
        mushroom = HFApi.cooking.newCategory("mushroom");
        juice_vegetable = HFApi.cooking.newCategory("vegetable_juice");
        salad_ingredient = HFApi.cooking.newCategory("salad_ingredient");
        sandwich_ingredient = HFApi.cooking.newCategory("sandwich_ingredient");
        sashimi_vegetable = HFApi.cooking.newCategory("sashimi_vegetable");

        //Seasonings
        salt = HFApi.cooking.newIngredient("salt", 0, 0, 0, 0.01F, 0);
        sugar = HFApi.cooking.newIngredient("sugar", 1, 0, 0, 0F, 0);

        //Store Bought
        chocolate = HFApi.cooking.newIngredient("chocolate", 5, -10, 1, 0.05F, 4);
        flour = HFApi.cooking.newIngredient("flour", 0, 0, 0, 0.1F, 4);
        oil = HFApi.cooking.newIngredient("oil", 0, -2, 0, 0F, 2).setFluid(MappingEvent.OIL);
        riceball = HFApi.cooking.newIngredient("riceball", 6, -3, 1, 0.085F, 16);

        //Sizeables
        egg = HFApi.cooking.newIngredient("egg", 6, 0, 0, 0.075F, 10);
        mayonnaise = HFApi.cooking.newIngredient("mayonnaise", 6, -1, 1, 0.8F, 8);
        milk = HFApi.cooking.newIngredient("milk", 5, -2, 0, 0.04F, 6).setFluid(MappingEvent.MILK);

        //Crops
        turnip = HFApi.cooking.newIngredient("turnip", 2, -1, 1, 0.033F, 8);
        potato = HFApi.cooking.newIngredient("potato", 5, -1, 1, 0.08F, 16);
        cucumber = HFApi.cooking.newIngredient("cucumber", 3, -1, 1, 0.02F, 4);
        strawberry = HFApi.cooking.newIngredient("strawberry", 3, -2, 1, 0.025F, 8);
        cabbage = HFApi.cooking.newIngredient("cabbage", 3, -3, 1, 0.05F, 8);
        tomato = HFApi.cooking.newIngredient("tomato", 2, -1, 1, 0.035F, 10);
        onion = HFApi.cooking.newIngredient("onion", 2, -2, 1, 0.03F, 8);
        corn = HFApi.cooking.newIngredient("corn", 5, -3, 1, 0.0375F, 16);
        pumpkin = HFApi.cooking.newIngredient("pumpkin", 4, -1, 2, 0.06F, 16);
        pineapple = HFApi.cooking.newIngredient("pineapple", 3, -3, 1, 0.055F, 16);
        eggplant = HFApi.cooking.newIngredient("eggplant", 2, -3, 1, 0.05F, 8);
        carrot = HFApi.cooking.newIngredient("carrot", 2, -2, 1, 0.035F, 6);
        sweet_potato = HFApi.cooking.newIngredient("potato_sweet", 6, -2, 1, 0.04F, 16);
        spinach = HFApi.cooking.newIngredient("spinach", 3, -4, 1, 0.022F, 8);
        green_pepper = HFApi.cooking.newIngredient("pepper_green", 4, -2, 1, 0.03F, 12);

        //Vanilla Stuff
        apple = HFApi.cooking.newIngredient("apple", 2, -1, 1, 0.015F, 6);
        bread = HFApi.cooking.newIngredient("bread", 8, -6, 3, 0.06F, 24);
        fish = HFApi.cooking.newIngredient("fish", 5, -5, 2, 0.1F, 16);
        watermelon = HFApi.cooking.newIngredient("watermelon", 1, -1, 1, 0.05F, 4);
        wheat = HFApi.cooking.newIngredient("wheat", 6, -3, 1, 0.0275F, 16);
        red_mushroom = HFApi.cooking.newIngredient("mushroom_red", 2, -1, 1, 0.02F, 8);
        brown_mushroom = HFApi.cooking.newIngredient("mushroom_brown", 2, -1, 1, 0.02F, 8);

        //Meals
        butter = HFApi.cooking.newIngredient("butter", 6, -1, 0, 0.01F, 6);
        boiled_egg = HFApi.cooking.newIngredient("egg.boiled", 10, -1, 2, 0.06F, 8);
        sashimi = HFApi.cooking.newIngredient("sashimi", 11, -2, 2, 0.07F, 10);
        scrambled_egg = HFApi.cooking.newIngredient("egg.scrambled", 20, -1, 1, 0.05F, 6);
        cookies = HFApi.cooking.newIngredient("cookies", 15, -2, 1, 0.03F, 4);
        //Idk if the numbers are right, check it yoshie
        ketchup = HFApi.cooking.newIngredient("ketchup", 2, -1, 1, 0.033F, 8);

        //Add ingredients to the categories
        mushroom.add(red_mushroom, brown_mushroom);
        juice_vegetable.add(turnip, cucumber, cabbage, tomato, onion, carrot, spinach, green_pepper);
        salad_ingredient.add(cucumber, carrot, tomato, cabbage, brown_mushroom);
        sandwich_ingredient.add(cucumber, tomato, mayonnaise, brown_mushroom, boiled_egg);
        sashimi_vegetable.add(cucumber, tomato, onion, eggplant);
    }

    public static void init() {
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(SALT), salt);
        HFApi.cooking.register(new ItemStack(Items.SUGAR, 1, OreDictionary.WILDCARD_VALUE), sugar);
        HFApi.cooking.register(new ItemStack(Items.APPLE, 1, OreDictionary.WILDCARD_VALUE), apple);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(CHOCOLATE), chocolate);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(FLOUR), flour);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(OIL), oil);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(RICEBALL), riceball);
        HFApi.cooking.register(HFCrops.TURNIP.getCropStack(), turnip);
        HFApi.cooking.register(HFCrops.POTATO.getCropStack(), potato);
        HFApi.cooking.register(new ItemStack(Items.POTATO, 1, OreDictionary.WILDCARD_VALUE), potato);
        HFApi.cooking.register(HFCrops.CUCUMBER.getCropStack(), cucumber);
        HFApi.cooking.register(HFCrops.STRAWBERRY.getCropStack(), strawberry);
        HFApi.cooking.register(HFCrops.CABBAGE.getCropStack(), cabbage);
        HFApi.cooking.register(HFCrops.ONION.getCropStack(), onion);
        HFApi.cooking.register(HFCrops.TOMATO.getCropStack(), tomato);
        HFApi.cooking.register(HFCrops.CORN.getCropStack(), corn);
        HFApi.cooking.register(HFCrops.PUMPKIN.getCropStack(), pumpkin);
        HFApi.cooking.register(new ItemStack(Blocks.PUMPKIN, 1, OreDictionary.WILDCARD_VALUE), pumpkin);
        HFApi.cooking.register(HFCrops.PINEAPPLE.getCropStack(), pineapple);
        HFApi.cooking.register(HFCrops.EGGPLANT.getCropStack(), eggplant);
        HFApi.cooking.register(HFCrops.CARROT.getCropStack(), carrot);
        HFApi.cooking.register(new ItemStack(Items.CARROT, 1, OreDictionary.WILDCARD_VALUE), carrot);
        HFApi.cooking.register(HFCrops.SWEET_POTATO.getCropStack(), sweet_potato);
        HFApi.cooking.register(HFCrops.SPINACH.getCropStack(), spinach);
        HFApi.cooking.register(HFCrops.GREEN_PEPPER.getCropStack(), green_pepper);
        HFApi.cooking.register(new ItemStack(Items.WHEAT, 1, OreDictionary.WILDCARD_VALUE), wheat);
        HFApi.cooking.register(new ItemStack(Items.MELON, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        HFApi.cooking.register(new ItemStack(Blocks.MELON_BLOCK, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        HFApi.cooking.register(new ItemStack(Items.BREAD, 1, OreDictionary.WILDCARD_VALUE), bread);
        HFApi.cooking.register(new ItemStack(HFAnimals.EGG), egg);
        HFApi.cooking.register(new ItemStack(Items.EGG, 1, OreDictionary.WILDCARD_VALUE), egg);
        HFApi.cooking.register(new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE), fish);
        HFApi.cooking.register(new ItemStack(HFAnimals.MILK), milk);
        HFApi.cooking.register(new ItemStack(Items.MILK_BUCKET, 1, OreDictionary.WILDCARD_VALUE), milk);
        HFApi.cooking.register(new ItemStack(HFAnimals.MAYONNAISE), mayonnaise);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(BUTTER), butter);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("butter"), boiled_egg);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("egg_boiled"), boiled_egg);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(SASHIMI), sashimi);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("sashimi"), sashimi);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(EGG_SCRAMBLED), scrambled_egg);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("egg_scrambled"), scrambled_egg);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(COOKIES), cookies);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("cookies"), cookies);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(KETCHUP), ketchup);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("ketchup"), ketchup);
        HFApi.cooking.register(new ItemStack(Blocks.BROWN_MUSHROOM, 1, OreDictionary.WILDCARD_VALUE), brown_mushroom);
        HFApi.cooking.register(new ItemStack(Blocks.RED_MUSHROOM, 1, OreDictionary.WILDCARD_VALUE), red_mushroom);
    }
}