package joshie.harvest.cooking;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.cooking.render.MappingEvent;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.crops.HFCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static joshie.harvest.cooking.items.ItemIngredients.Ingredient.*;
import static joshie.harvest.core.lib.LoadOrder.HFINGREDIENTS;

@HFLoader(priority = HFINGREDIENTS)
public class HFIngredients {
    //Categories
    public static ICookingIngredient mushroom;
    public static ICookingIngredient juice_vegetable;
    public static ICookingIngredient salad_ingredient;
    public static ICookingIngredient sandwich_ingredient;
    public static ICookingIngredient sashimi_vegetable;

    //Seasonings
    public static ICookingIngredient salt;
    public static ICookingIngredient sugar;

    //Meals
    public static ICookingIngredient butter;
    public static ICookingIngredient boiled_egg;
    public static ICookingIngredient sashimi;
    public static ICookingIngredient scrambled_egg;
    public static ICookingIngredient cookies;

    //Random Stuff
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

    //Crops
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
        salt = HFApi.cooking.newIngredient("salt", 0, 0.05F, -5F, 0);
        sugar = HFApi.cooking.newIngredient("sugar", 2, 0.05F, 0F, 0);

        //Store Bought ///Stamina, Fatigue, Hunger, Saturation >
        chocolate = HFApi.cooking.newIngredient("chocolate", 1, 0.28F, -10F, 4);
        flour = HFApi.cooking.newIngredient("flour", 0, 0.3F, 0F, 4);
        oil = HFApi.cooking.newIngredient("oil", 0, 0.0F, -3F, 2).setFluid(MappingEvent.OIL);
        riceball = HFApi.cooking.newIngredient("riceball", 1, 0.34F, -3F, 16);

        //Sizeables
        egg = HFApi.cooking.newIngredient("egg", 1, 0.34F, 0F, 10);
        mayonnaise = HFApi.cooking.newIngredient("mayonnaise", 1, 0.25F, -1F, 8);
        milk = HFApi.cooking.newIngredient("milk", 1, 0.2F, -2F, 6).setFluid(MappingEvent.MILK);

        //Crops
        turnip = HFApi.cooking.newIngredient("turnip", 1, 0.12F, -1F, 8);
        potato = HFApi.cooking.newIngredient("potato", 2, 0.28F, -1F, 16);
        cucumber = HFApi.cooking.newIngredient("cucumber", 1, 0.18F, -1F, 4);
        strawberry = HFApi.cooking.newIngredient("strawberry", 1, 0.18F, -2F, 8);
        cabbage = HFApi.cooking.newIngredient("cabbage", 1, 0.18F, -3F, 8);
        tomato = HFApi.cooking.newIngredient("tomato", 1, 0.12F, -1F, 10);
        onion = HFApi.cooking.newIngredient("onion", 1, 0.12F, -2F, 8);
        corn = HFApi.cooking.newIngredient("corn", 2, 0.28F, -3F, 16);
        pumpkin = HFApi.cooking.newIngredient("pumpkin", 2, 0.22F, -1F, 16);
        pineapple = HFApi.cooking.newIngredient("pineapple", 1, 0.18F, -3F, 16);
        eggplant = HFApi.cooking.newIngredient("eggplant", 1, 0.12F, -3F, 8);
        carrot = HFApi.cooking.newIngredient("carrot", 1, 0.12F, -2F, 6);
        sweet_potato = HFApi.cooking.newIngredient("sweet_potato", 2, 0.34F, -2F, 16);
        spinach = HFApi.cooking.newIngredient("spinach", 1, 0.17F, -4F, 8);
        green_pepper = HFApi.cooking.newIngredient("green_pepper", 2, 0.22F, -2F, 12);

        //Vanilla Stuff
        apple = HFApi.cooking.newIngredient("apple", 1, 0.12F, -1F, 6);
        bread = HFApi.cooking.newIngredient("bread", 3, 0.46F, -6F, 24);
        fish = HFApi.cooking.newIngredient("fish", 2, 0.28F, -5F, 16);
        watermelon = HFApi.cooking.newIngredient("watermelon", 1, 0.06F, -1F, 4);
        wheat = HFApi.cooking.newIngredient("wheat", 2, 0.34F, -3F, 16);
        red_mushroom = HFApi.cooking.newIngredient("red_mushroom", 1, 0.12F, -1F, 8);
        brown_mushroom = HFApi.cooking.newIngredient("brown_mushroom", 1, 0.12F, -1F, 8);

        //Meals
        butter = HFApi.cooking.newIngredient("butter", 0, 0.17F, -1F, 6);
        boiled_egg = HFApi.cooking.newIngredient("boiled_egg", 3, 0.58F, -1F, 8);
        sashimi = HFApi.cooking.newIngredient("sashimi", 4, 0.62F, -2F, 10);
        scrambled_egg = HFApi.cooking.newIngredient("scrambled_egg", 6, 1.07F, -1F, 6);
        cookies = HFApi.cooking.newIngredient("cookies", 5, 0.86F, -2F, 4);
        ketchup = HFApi.cooking.newIngredient("ketchup", 0, 0.06F, -1F, 2);

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
        HFApi.cooking.register(new ItemStack(Items.FISH, 1, 0), fish);
        HFApi.cooking.register(new ItemStack(Items.FISH, 1, 1), fish);
        HFApi.cooking.register(new ItemStack(HFAnimals.MILK), milk);
        HFApi.cooking.register(new ItemStack(Items.MILK_BUCKET, 1, OreDictionary.WILDCARD_VALUE), milk);
        HFApi.cooking.register(new ItemStack(HFAnimals.MAYONNAISE), mayonnaise);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(BUTTER), butter);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("butter"), butter);
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