package joshie.harvest.cooking.recipe;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients;
import joshie.harvest.cooking.render.MappingEvent;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.crops.HFCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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

    //Formula
    //Cost = Shop/Sell Price += Give or Take
    //Food = Real Food
    //Sat = Real Sat
    //Exhaustion = Random
    //Eat Time =

    //Seasonings
    public static final Ingredient SALT = new Ingredient("salt", 0, 0.05F).setEatTime(0);
    public static final Ingredient SUGAR = new Ingredient("sugar", 2, 0.05F).setEatTime(0);

    //Meals
    public static final Ingredient BUTTER = new Ingredient("butter", 1, 0.06F).setEatTime(-8);
    public static final Ingredient BOILED_EGG = new Ingredient("boiled_egg", 6, 1.14F);
    public static final Ingredient SASHIMI = new Ingredient("sashimi", 6, 1.26F);
    public static final Ingredient SCRAMBLED_EGG = new Ingredient("scrambled_egg", 11, 2.28F);
    public static final Ingredient COOKIES = new Ingredient("cookies", 4, 0.87F);
    public static final Ingredient KETCHUP = new Ingredient("ketchup", 1, 0.06F).setEatTime(0);

    //Random Stuff
    public static final Ingredient APPLE = new Ingredient("apple", 4, 0.6F);
    public static final Ingredient CHOCOLATE = new Ingredient("chocolate", 2, 0.5F);
    public static final Ingredient EGG = new Ingredient("egg", 1, 0.34F);
    public static final Ingredient FLOUR = new Ingredient("flour", 1, 0.2F);
    public static final Ingredient OIL = new Ingredient("oil", 0, 0.1F).setFluid(MappingEvent.OIL).setEatTime(-8);
    public static final Ingredient RICEBALL = new Ingredient("riceball", 1, 0.25F);
    public static final Ingredient MILK = new Ingredient("milk", 1, 0.2F).setFluid(MappingEvent.MILK);
    public static final Ingredient MAYONNAISE = new Ingredient("mayonnaise", 1, 0.5F);
    public static final Ingredient BREAD = new Ingredient("bread", 5, 1.2F);
    public static final Ingredient RED_MUSHROOM = new Ingredient("red_mushroom", 4, 0.5F);
    public static final Ingredient BROWN_MUSHROOM = new Ingredient("brown_mushroom", 2, 0.7F);
    public static final Ingredient BAKED_POTATO = new Ingredient("baked_potato", 5, 1.2F);

    //Crops
    public static final Ingredient TURNIP = HFCrops.TURNIP.getIngredient();
    public static final Ingredient POTATO = HFCrops.POTATO.getIngredient();
    public static final Ingredient CUCUMBER = HFCrops.CUCUMBER.getIngredient();
    public static final Ingredient STRAWBERRY = HFCrops.STRAWBERRY.getIngredient();
    public static final Ingredient CABBAGE = HFCrops.CABBAGE.getIngredient();
    public static final Ingredient TOMATO = HFCrops.TOMATO.getIngredient();
    public static final Ingredient ONION = HFCrops.ONION.getIngredient();
    public static final Ingredient CORN = HFCrops.CORN.getIngredient();
    public static final Ingredient PUMPKIN = HFCrops.PUMPKIN.getIngredient();
    public static final Ingredient PINEAPPLE = HFCrops.PINEAPPLE.getIngredient();
    public static final Ingredient EGGPLANT = HFCrops.EGGPLANT.getIngredient();
    public static final Ingredient CARROT = HFCrops.CARROT.getIngredient();
    public static final Ingredient SWEET_POTATO = HFCrops.SWEET_POTATO.getIngredient();
    public static final Ingredient SPINACH = HFCrops.SPINACH.getIngredient();
    public static final Ingredient GREEN_PEPPER = HFCrops.GREEN_PEPPER.getIngredient();
    public static final Ingredient BEETROOT = HFCrops.BEETROOT.getIngredient();
    public static final Ingredient WATERMELON = HFCrops.WATERMELON.getIngredient();
    public static final Ingredient WHEAT = HFCrops.WHEAT.getIngredient();

    //Meats
    public static final Ingredient CHICKEN = new Ingredient("chicken", 2, 0.6F);
    public static final Ingredient PORK = new Ingredient("pork", 3, 0.6F);
    public static final Ingredient BEEF = new Ingredient("beef", 3, 0.6F);
    public static final Ingredient MUTTON = new Ingredient("mutton", 2, 0.6F);
    public static final Ingredient SALMON = new Ingredient("salmon", 2, 0.2F);
    public static final Ingredient COD = new Ingredient("cod", 2, 0.2F);
    public static final Ingredient RABBIT = new Ingredient("rabbit", 3, 0.6F);
    public static final Ingredient RABBIT_COOKED = new Ingredient("cooked_rabbit", 5, 1.2F);

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

    public static void postInit() {
        //Animal Products
        CookingAPI.INSTANCE.register(new ItemStack(Items.EGG), EGG);
        CookingAPI.INSTANCE.register(new ItemStack(Items.MILK_BUCKET), MILK);
        CookingAPI.INSTANCE.register(HFAnimals.EGG.getStack(Size.SMALL), EGG);
        CookingAPI.INSTANCE.register(HFAnimals.MILK.getStack(Size.SMALL), MILK);
        CookingAPI.INSTANCE.register(HFAnimals.MAYONNAISE.getStack(Size.SMALL), MAYONNAISE);

        //Crops
        for (Crop crop: Crop.REGISTRY) {
            if (crop.getCropStack() != null) {
                if (crop.getCropStack().getItem() == HFCrops.CROP) {
                    CookingAPI.INSTANCE.register(crop.getCropStack(), crop.getIngredient());
                }
            }
        }

        //Fruits
        CookingAPI.INSTANCE.register(new ItemStack(Items.APPLE), APPLE);
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.MELON_BLOCK), WATERMELON);

        //Fungus
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.BROWN_MUSHROOM), BROWN_MUSHROOM);
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.RED_MUSHROOM), RED_MUSHROOM);

        //Ingredients
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.CHOCOLATE), CHOCOLATE);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.FLOUR), FLOUR);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.OIL), OIL);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.RICEBALL), RICEBALL);

        //Meals - Real
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("butter"), BUTTER);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("egg_boiled"), BOILED_EGG);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("sashimi"), SASHIMI);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("egg_scrambled"), SCRAMBLED_EGG);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("cookies"), COOKIES);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("ketchup"), KETCHUP);
        //Meals - Alts
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.BUTTER), BUTTER);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SASHIMI), SASHIMI);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.EGG_SCRAMBLED), SCRAMBLED_EGG);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.COOKIES), COOKIES);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.KETCHUP), KETCHUP);
        //Meals - Vanilla
        CookingAPI.INSTANCE.register(new ItemStack(Items.BREAD), BREAD);
        CookingAPI.INSTANCE.register(new ItemStack(Items.BAKED_POTATO), BAKED_POTATO);

        //Meats
        CookingAPI.INSTANCE.register(new ItemStack(Items.FISH, 1, 0), COD);
        CookingAPI.INSTANCE.register(new ItemStack(Items.FISH, 1, 1), SALMON);
        CookingAPI.INSTANCE.register(new ItemStack(Items.CHICKEN), CHICKEN);
        CookingAPI.INSTANCE.register(new ItemStack(Items.RABBIT), RABBIT);
        CookingAPI.INSTANCE.register(new ItemStack(Items.BEEF), BEEF);
        CookingAPI.INSTANCE.register(new ItemStack(Items.PORKCHOP), PORK);
        CookingAPI.INSTANCE.register(new ItemStack(Items.MUTTON), MUTTON);
        CookingAPI.INSTANCE.register(new ItemStack(Items.COOKED_RABBIT), RABBIT_COOKED);

        //Spices and Salts
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SALT), SALT);
        CookingAPI.INSTANCE.register(new ItemStack(Items.SUGAR), SUGAR);
    }
}