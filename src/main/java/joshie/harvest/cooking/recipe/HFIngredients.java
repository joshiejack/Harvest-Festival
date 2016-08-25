package joshie.harvest.cooking.recipe;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.items.ItemIngredients;
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
    public static final ICookingIngredient MUSHROOM = HFApi.cooking.newCategory("mushroom");
    public static final ICookingIngredient JUICE_VEGETABLE = HFApi.cooking.newCategory("vegetable_juice");
    public static final ICookingIngredient SALAD_INGREDIENT = HFApi.cooking.newCategory("salad_ingredient");
    public static final ICookingIngredient SANDWICH_INGREDIENT = HFApi.cooking.newCategory("sandwich_ingredient");
    public static final ICookingIngredient SASHIMI_VEGETABLE = HFApi.cooking.newCategory("sashimi_vegetable");
    public static final ICookingIngredient FISH = HFApi.cooking.newCategory("fish");
    public static final ICookingIngredient MEAT = HFApi.cooking.newCategory("meat");

    //Seasonings
    public static final ICookingIngredient SALT = HFApi.cooking.newIngredient("salt", 0, 0.05F, -5F, 0);
    public static final ICookingIngredient SUGAR = HFApi.cooking.newIngredient("sugar", 2, 0.05F, 0F, 0);

    //Meals
    public static final ICookingIngredient BUTTER = HFApi.cooking.newIngredient("butter", 0, 0.17F, -1F, 6);
    public static final ICookingIngredient BOILED_EGG = HFApi.cooking.newIngredient("boiled_egg", 3, 0.58F, -1F, 8);
    public static final ICookingIngredient SASHIMI = HFApi.cooking.newIngredient("sashimi", 4, 0.62F, -2F, 10);
    public static final ICookingIngredient SCRAMBLED_EGG = HFApi.cooking.newIngredient("scrambled_egg", 6, 1.07F, -1F, 6);
    public static final ICookingIngredient COOKIES = HFApi.cooking.newIngredient("cookies", 5, 0.86F, -2F, 4);
    public static final ICookingIngredient KETCHUP = HFApi.cooking.newIngredient("ketchup", 0, 0.06F, -1F, 2);

    //Random Stuff
    public static final ICookingIngredient APPLE = HFApi.cooking.newIngredient("apple", 1, 0.12F, -1F, 6);
    public static final ICookingIngredient CHOCOLATE = HFApi.cooking.newIngredient("chocolate", 1, 0.28F, -10F, 4);
    public static final ICookingIngredient EGG = HFApi.cooking.newIngredient("egg", 1, 0.34F, 0F, 10);
    public static final ICookingIngredient FLOUR = HFApi.cooking.newIngredient("flour", 0, 0.3F, 0F, 4);
    public static final ICookingIngredient OIL = HFApi.cooking.newIngredient("oil", 0, 0.0F, -3F, 2).setFluid(MappingEvent.OIL);
    public static final ICookingIngredient RICEBALL = HFApi.cooking.newIngredient("riceball", 1, 0.34F, -3F, 16);
    public static final ICookingIngredient MILK = HFApi.cooking.newIngredient("milk", 1, 0.2F, -2F, 6).setFluid(MappingEvent.MILK);
    public static final ICookingIngredient MAYONNAISE = HFApi.cooking.newIngredient("mayonnaise", 1, 0.25F, -1F, 8);
    public static final ICookingIngredient BREAD = HFApi.cooking.newIngredient("bread", 3, 0.46F, -6F, 24);
    public static final ICookingIngredient RED_MUSHROOM = HFApi.cooking.newIngredient("red_mushroom", 1, 0.12F, -1F, 8);
    public static final ICookingIngredient BROWN_MUSHROOM = HFApi.cooking.newIngredient("brown_mushroom", 1, 0.12F, -1F, 8);
    public static final ICookingIngredient BAKED_POTATO = HFApi.cooking.newIngredient("baked_potato", 4, 0.33F, -1F, 16);

    //Crops
    public static final ICookingIngredient TURNIP = HFApi.cooking.newIngredient("turnip", 1, 0.12F, -1F, 8);
    public static final ICookingIngredient POTATO = HFApi.cooking.newIngredient("potato", 2, 0.28F, -1F, 16);
    public static final ICookingIngredient CUCUMBER = HFApi.cooking.newIngredient("cucumber", 1, 0.18F, -1F, 4);
    public static final ICookingIngredient STRAWBERRY = HFApi.cooking.newIngredient("strawberry", 1, 0.18F, -2F, 8);
    public static final ICookingIngredient CABBAGE = HFApi.cooking.newIngredient("cabbage", 1, 0.18F, -3F, 8);
    public static final ICookingIngredient TOMATO = HFApi.cooking.newIngredient("tomato", 1, 0.12F, -1F, 10);
    public static final ICookingIngredient ONION = HFApi.cooking.newIngredient("onion", 1, 0.12F, -2F, 8);
    public static final ICookingIngredient CORN = HFApi.cooking.newIngredient("corn", 2, 0.28F, -3F, 16);
    public static final ICookingIngredient PUMPKIN = HFApi.cooking.newIngredient("pumpkin", 2, 0.22F, -1F, 16);
    public static final ICookingIngredient PINEAPPLE = HFApi.cooking.newIngredient("pineapple", 1, 0.18F, -3F, 16);
    public static final ICookingIngredient EGGPLANT = HFApi.cooking.newIngredient("eggplant", 1, 0.12F, -3F, 8);
    public static final ICookingIngredient CARROT = HFApi.cooking.newIngredient("carrot", 1, 0.12F, -2F, 6);
    public static final ICookingIngredient SWEET_POTATO = HFApi.cooking.newIngredient("sweet_potato", 2, 0.34F, -2F, 16);
    public static final ICookingIngredient SPINACH = HFApi.cooking.newIngredient("spinach", 1, 0.17F, -4F, 8);
    public static final ICookingIngredient GREEN_PEPPER = HFApi.cooking.newIngredient("green_pepper", 2, 0.22F, -2F, 12);
    public static final ICookingIngredient BEETROOT = HFApi.cooking.newIngredient("beetroot", 1, 0.05F, -1F, 4);
    public static final ICookingIngredient WATERMELON = HFApi.cooking.newIngredient("watermelon", 1, 0.06F, -1F, 4);
    public static final ICookingIngredient WHEAT = HFApi.cooking.newIngredient("wheat", 2, 0.34F, -3F, 16);

    //Meats
    public static final ICookingIngredient CHICKEN = HFApi.cooking.newIngredient("chicken", 2, 0.3F, 0F, 8);
    public static final ICookingIngredient PORK = HFApi.cooking.newIngredient("pork", 3, 0.35F, 0F, 8);
    public static final ICookingIngredient BEEF = HFApi.cooking.newIngredient("beef", 4, 0.4F, 0F, 12);
    public static final ICookingIngredient MUTTON = HFApi.cooking.newIngredient("mutton", 1, 0.5F, 0F, 12);
    public static final ICookingIngredient SALMON = HFApi.cooking.newIngredient("salmon", 2, 0.4F, 0F, 4);
    public static final ICookingIngredient COD = HFApi.cooking.newIngredient("cod", 1, 0.5F, 0F, 12);
    public static final ICookingIngredient RABBIT = HFApi.cooking.newIngredient("rabbit", 2, 0.4F, 0F, 4);
    public static final ICookingIngredient RABBIT_COOKED = HFApi.cooking.newIngredient("cooked_rabbit", 3, 0.5F, 0F, 16);

    public static void preInit() {
        //Add ingredients to the categories
        MUSHROOM.add(RED_MUSHROOM, BROWN_MUSHROOM);
        JUICE_VEGETABLE.add(TURNIP, CUCUMBER, CABBAGE, TOMATO, ONION, CARROT, SPINACH, GREEN_PEPPER);
        SALAD_INGREDIENT.add(CUCUMBER, CARROT, TOMATO, CABBAGE, BROWN_MUSHROOM);
        SANDWICH_INGREDIENT.add(CUCUMBER, TOMATO, MAYONNAISE, BROWN_MUSHROOM, BOILED_EGG);
        SASHIMI_VEGETABLE.add(CUCUMBER, TOMATO, ONION, EGGPLANT);
        MEAT.add(CHICKEN, PORK, BEEF, MUTTON, RABBIT);
        FISH.add(SALMON, COD);
    }

    public static void init() {
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SALT), SALT);
        HFApi.cooking.register(new ItemStack(Items.SUGAR), SUGAR);
        HFApi.cooking.register(new ItemStack(Items.APPLE), APPLE);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.CHOCOLATE), CHOCOLATE);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.FLOUR), FLOUR);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.OIL), OIL);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.RICEBALL), RICEBALL);
        HFApi.cooking.register(HFCrops.TURNIP.getCropStack(), TURNIP);
        HFApi.cooking.register(HFCrops.POTATO.getCropStack(), POTATO);
        HFApi.cooking.register(HFCrops.BEETROOT.getCropStack(), BEETROOT);
        HFApi.cooking.register(HFCrops.CUCUMBER.getCropStack(), CUCUMBER);
        HFApi.cooking.register(HFCrops.STRAWBERRY.getCropStack(), STRAWBERRY);
        HFApi.cooking.register(HFCrops.CABBAGE.getCropStack(), CABBAGE);
        HFApi.cooking.register(HFCrops.ONION.getCropStack(), ONION);
        HFApi.cooking.register(HFCrops.TOMATO.getCropStack(), TOMATO);
        HFApi.cooking.register(HFCrops.CORN.getCropStack(), CORN);
        HFApi.cooking.register(HFCrops.PUMPKIN.getCropStack(), PUMPKIN);
        HFApi.cooking.register(HFCrops.PINEAPPLE.getCropStack(), PINEAPPLE);
        HFApi.cooking.register(HFCrops.EGGPLANT.getCropStack(), EGGPLANT);
        HFApi.cooking.register(HFCrops.CARROT.getCropStack(), CARROT);
        HFApi.cooking.register(HFCrops.SWEET_POTATO.getCropStack(), SWEET_POTATO);
        HFApi.cooking.register(HFCrops.SPINACH.getCropStack(), SPINACH);
        HFApi.cooking.register(HFCrops.GREEN_PEPPER.getCropStack(), GREEN_PEPPER);
        HFApi.cooking.register(new ItemStack(Items.WHEAT), WHEAT);
        HFApi.cooking.register(new ItemStack(Items.MELON), WATERMELON);
        HFApi.cooking.register(new ItemStack(Blocks.MELON_BLOCK), WATERMELON);
        HFApi.cooking.register(new ItemStack(Items.BREAD), BREAD);
        HFApi.cooking.register(new ItemStack(Items.BAKED_POTATO), BAKED_POTATO);
        HFApi.cooking.register(new ItemStack(HFAnimals.EGG), EGG);
        HFApi.cooking.register(new ItemStack(Items.EGG), EGG);
        HFApi.cooking.register(new ItemStack(HFAnimals.MILK), MILK);
        HFApi.cooking.register(new ItemStack(Items.MILK_BUCKET, 1, OreDictionary.WILDCARD_VALUE), MILK);
        HFApi.cooking.register(new ItemStack(HFAnimals.MAYONNAISE), MAYONNAISE);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.BUTTER), BUTTER);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("butter"), BUTTER);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("egg_boiled"), BOILED_EGG);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SASHIMI), SASHIMI);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("sashimi"), SASHIMI);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(EGG_SCRAMBLED), SCRAMBLED_EGG);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("egg_scrambled"), SCRAMBLED_EGG);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.COOKIES), COOKIES);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("cookies"), COOKIES);
        HFApi.cooking.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.KETCHUP), KETCHUP);
        HFApi.cooking.register(HFApi.cooking.getBestMeal("ketchup"), KETCHUP);
        HFApi.cooking.register(new ItemStack(Blocks.BROWN_MUSHROOM), BROWN_MUSHROOM);
        HFApi.cooking.register(new ItemStack(Blocks.RED_MUSHROOM), RED_MUSHROOM);

        //Meats
        HFApi.cooking.register(new ItemStack(Items.FISH, 1, 0), COD);
        HFApi.cooking.register(new ItemStack(Items.FISH, 1, 1), SALMON);
        HFApi.cooking.register(new ItemStack(Items.CHICKEN), CHICKEN);
        HFApi.cooking.register(new ItemStack(Items.RABBIT), RABBIT);
        HFApi.cooking.register(new ItemStack(Items.BEEF), BEEF);
        HFApi.cooking.register(new ItemStack(Items.PORKCHOP), PORK);
        HFApi.cooking.register(new ItemStack(Items.MUTTON), MUTTON);
        HFApi.cooking.register(new ItemStack(Items.COOKED_RABBIT), RABBIT_COOKED);
    }
}