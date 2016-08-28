package joshie.harvest.cooking.recipe;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients;
import joshie.harvest.cooking.render.MappingEvent;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.crops.HFCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static joshie.harvest.cooking.item.ItemIngredients.Ingredient.EGG_SCRAMBLED;
import static joshie.harvest.core.lib.LoadOrder.HFINGREDIENTS;

@HFLoader(priority = HFINGREDIENTS)
public class HFIngredients {
    //Categories
    public static final Ingredient MUSHROOM = new Ingredient("mushroom");
    public static final Ingredient JUICE_VEGETABLE = new Ingredient("vegetable_juice");
    public static final Ingredient SALAD_INGREDIENT = new Ingredient("salad_ingredient");
    public static final Ingredient SANDWICH_INGREDIENT = new Ingredient("sandwich_ingredient");
    public static final Ingredient SASHIMI_VEGETABLE = new Ingredient("sashimi_vegetable");
    public static final Ingredient FISH = new Ingredient("fish");
    public static final Ingredient MEAT = new Ingredient("meat");

    //Seasonings
    public static final Ingredient SALT = new Ingredient("salt", 0, 0.05F, -5F, 0);
    public static final Ingredient SUGAR = new Ingredient("sugar", 2, 0.05F, 0F, 0);

    //Meals
    public static final Ingredient BUTTER = new Ingredient("butter", 0, 0.17F, -1F, 6);
    public static final Ingredient BOILED_EGG = new Ingredient("boiled_egg", 3, 0.58F, -1F, 8);
    public static final Ingredient SASHIMI = new Ingredient("sashimi", 4, 0.62F, -2F, 10);
    public static final Ingredient SCRAMBLED_EGG = new Ingredient("scrambled_egg", 6, 1.07F, -1F, 6);
    public static final Ingredient COOKIES = new Ingredient("cookies", 5, 0.86F, -2F, 4);
    public static final Ingredient KETCHUP = new Ingredient("ketchup", 0, 0.06F, -1F, 2);

    //Random Stuff
    public static final Ingredient APPLE = new Ingredient("apple", 1, 0.12F, -1F, 6);
    public static final Ingredient CHOCOLATE = new Ingredient("chocolate", 1, 0.28F, -10F, 4);
    public static final Ingredient EGG = new Ingredient("egg", 1, 0.34F, 0F, 10);
    public static final Ingredient FLOUR = new Ingredient("flour", 0, 0.3F, 0F, 4);
    public static final Ingredient OIL = new Ingredient("oil", 0, 0.0F, -3F, 2).setFluid(MappingEvent.OIL);
    public static final Ingredient RICEBALL = new Ingredient("riceball", 1, 0.34F, -3F, 16);
    public static final Ingredient MILK = new Ingredient("milk", 1, 0.2F, -2F, 6).setFluid(MappingEvent.MILK);
    public static final Ingredient MAYONNAISE = new Ingredient("mayonnaise", 1, 0.25F, -1F, 8);
    public static final Ingredient BREAD = new Ingredient("bread", 3, 0.46F, -6F, 24);
    public static final Ingredient RED_MUSHROOM = new Ingredient("red_mushroom", 1, 0.12F, -1F, 8);
    public static final Ingredient BROWN_MUSHROOM = new Ingredient("brown_mushroom", 1, 0.12F, -1F, 8);
    public static final Ingredient BAKED_POTATO = new Ingredient("baked_potato", 4, 0.33F, -1F, 16);

    //Crops
    public static final Ingredient TURNIP = new Ingredient("turnip", 1, 0.12F, -1F, 8);
    public static final Ingredient POTATO = new Ingredient("potato", 2, 0.28F, -1F, 16);
    public static final Ingredient CUCUMBER = new Ingredient("cucumber", 1, 0.18F, -1F, 4);
    public static final Ingredient STRAWBERRY = new Ingredient("strawberry", 1, 0.18F, -2F, 8);
    public static final Ingredient CABBAGE = new Ingredient("cabbage", 1, 0.18F, -3F, 8);
    public static final Ingredient TOMATO = new Ingredient("tomato", 1, 0.12F, -1F, 10);
    public static final Ingredient ONION = new Ingredient("onion", 1, 0.12F, -2F, 8);
    public static final Ingredient CORN = new Ingredient("corn", 2, 0.28F, -3F, 16);
    public static final Ingredient PUMPKIN = new Ingredient("pumpkin", 2, 0.22F, -1F, 16);
    public static final Ingredient PINEAPPLE = new Ingredient("pineapple", 1, 0.18F, -3F, 16);
    public static final Ingredient EGGPLANT = new Ingredient("eggplant", 1, 0.12F, -3F, 8);
    public static final Ingredient CARROT = new Ingredient("carrot", 1, 0.12F, -2F, 6);
    public static final Ingredient SWEET_POTATO = new Ingredient("sweet_potato", 2, 0.34F, -2F, 16);
    public static final Ingredient SPINACH = new Ingredient("spinach", 1, 0.17F, -4F, 8);
    public static final Ingredient GREEN_PEPPER = new Ingredient("green_pepper", 2, 0.22F, -2F, 12);
    public static final Ingredient BEETROOT = new Ingredient("beetroot", 1, 0.05F, -1F, 4);
    public static final Ingredient WATERMELON = new Ingredient("watermelon", 1, 0.06F, -1F, 4);
    public static final Ingredient WHEAT = new Ingredient("wheat", 2, 0.34F, -3F, 16);

    //Meats
    public static final Ingredient CHICKEN = new Ingredient("chicken", 2, 0.3F, 0F, 8);
    public static final Ingredient PORK = new Ingredient("pork", 3, 0.35F, 0F, 8);
    public static final Ingredient BEEF = new Ingredient("beef", 4, 0.4F, 0F, 12);
    public static final Ingredient MUTTON = new Ingredient("mutton", 1, 0.5F, 0F, 12);
    public static final Ingredient SALMON = new Ingredient("salmon", 2, 0.4F, 0F, 4);
    public static final Ingredient COD = new Ingredient("cod", 1, 0.5F, 0F, 12);
    public static final Ingredient RABBIT = new Ingredient("rabbit", 2, 0.4F, 0F, 4);
    public static final Ingredient RABBIT_COOKED = new Ingredient("cooked_rabbit", 3, 0.5F, 0F, 16);

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

    public static void remap() {
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SALT), SALT);
        CookingAPI.INSTANCE.register(new ItemStack(Items.SUGAR), SUGAR);
        CookingAPI.INSTANCE.register(new ItemStack(Items.APPLE), APPLE);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.CHOCOLATE), CHOCOLATE);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.FLOUR), FLOUR);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.OIL), OIL);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.RICEBALL), RICEBALL);
        CookingAPI.INSTANCE.register(HFCrops.TURNIP.getCropStack(), TURNIP);
        CookingAPI.INSTANCE.register(HFCrops.POTATO.getCropStack(), POTATO);
        CookingAPI.INSTANCE.register(HFCrops.BEETROOT.getCropStack(), BEETROOT);
        CookingAPI.INSTANCE.register(HFCrops.CUCUMBER.getCropStack(), CUCUMBER);
        CookingAPI.INSTANCE.register(HFCrops.STRAWBERRY.getCropStack(), STRAWBERRY);
        CookingAPI.INSTANCE.register(HFCrops.CABBAGE.getCropStack(), CABBAGE);
        CookingAPI.INSTANCE.register(HFCrops.ONION.getCropStack(), ONION);
        CookingAPI.INSTANCE.register(HFCrops.TOMATO.getCropStack(), TOMATO);
        CookingAPI.INSTANCE.register(HFCrops.CORN.getCropStack(), CORN);
        CookingAPI.INSTANCE.register(HFCrops.PUMPKIN.getCropStack(), PUMPKIN);
        CookingAPI.INSTANCE.register(HFCrops.PINEAPPLE.getCropStack(), PINEAPPLE);
        CookingAPI.INSTANCE.register(HFCrops.EGGPLANT.getCropStack(), EGGPLANT);
        CookingAPI.INSTANCE.register(HFCrops.CARROT.getCropStack(), CARROT);
        CookingAPI.INSTANCE.register(HFCrops.SWEET_POTATO.getCropStack(), SWEET_POTATO);
        CookingAPI.INSTANCE.register(HFCrops.SPINACH.getCropStack(), SPINACH);
        CookingAPI.INSTANCE.register(HFCrops.GREEN_PEPPER.getCropStack(), GREEN_PEPPER);
        CookingAPI.INSTANCE.register(new ItemStack(Items.WHEAT), WHEAT);
        CookingAPI.INSTANCE.register(new ItemStack(Items.MELON), WATERMELON);
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.MELON_BLOCK), WATERMELON);
        CookingAPI.INSTANCE.register(new ItemStack(Items.BREAD), BREAD);
        CookingAPI.INSTANCE.register(new ItemStack(Items.BAKED_POTATO), BAKED_POTATO);
        CookingAPI.INSTANCE.register(new ItemStack(HFAnimals.EGG), EGG);
        CookingAPI.INSTANCE.register(new ItemStack(Items.EGG), EGG);
        CookingAPI.INSTANCE.register(new ItemStack(HFAnimals.MILK), MILK);
        CookingAPI.INSTANCE.register(new ItemStack(Items.MILK_BUCKET, 1, OreDictionary.WILDCARD_VALUE), MILK);
        CookingAPI.INSTANCE.register(new ItemStack(HFAnimals.MAYONNAISE), MAYONNAISE);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.BUTTER), BUTTER);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("butter"), BUTTER);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("egg_boiled"), BOILED_EGG);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SASHIMI), SASHIMI);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("sashimi"), SASHIMI);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(EGG_SCRAMBLED), SCRAMBLED_EGG);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("egg_scrambled"), SCRAMBLED_EGG);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.COOKIES), COOKIES);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("cookies"), COOKIES);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.KETCHUP), KETCHUP);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("ketchup"), KETCHUP);
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.BROWN_MUSHROOM), BROWN_MUSHROOM);
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.RED_MUSHROOM), RED_MUSHROOM);

        //Meats
        CookingAPI.INSTANCE.register(new ItemStack(Items.FISH, 1, 0), COD);
        CookingAPI.INSTANCE.register(new ItemStack(Items.FISH, 1, 1), SALMON);
        CookingAPI.INSTANCE.register(new ItemStack(Items.CHICKEN), CHICKEN);
        CookingAPI.INSTANCE.register(new ItemStack(Items.RABBIT), RABBIT);
        CookingAPI.INSTANCE.register(new ItemStack(Items.BEEF), BEEF);
        CookingAPI.INSTANCE.register(new ItemStack(Items.PORKCHOP), PORK);
        CookingAPI.INSTANCE.register(new ItemStack(Items.MUTTON), MUTTON);
        CookingAPI.INSTANCE.register(new ItemStack(Items.COOKED_RABBIT), RABBIT_COOKED);
    }
}