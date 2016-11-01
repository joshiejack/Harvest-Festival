package joshie.harvest.cooking.recipe;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients;
import joshie.harvest.cooking.render.MappingEvent;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.item.ItemSizeable.Sizeable;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.gathering.block.BlockNature.NaturalBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static joshie.harvest.core.lib.LoadOrder.HFINGREDIENTS;

@HFLoader(priority = HFINGREDIENTS)
public class HFIngredients {
    //Categories
    public static final Ingredient MUSHROOM = new Ingredient("mushroom");
    public static final Ingredient VEGETABLE_JUICE_BASE = new Ingredient("vegetable_juice");
    public static final Ingredient FRUIT_JUICE_BASE = new Ingredient("fruit_juice");
    public static final Ingredient SALAD_BASE = new Ingredient("salad_ingredient");
    public static final Ingredient SANDWICH_BASE = new Ingredient("sandwich_ingredient");
    public static final Ingredient FRUITS = new Ingredient("sandwich_fruit");
    public static final Ingredient CAKE_FRUIT = new Ingredient("cake_fruit");
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
    public static final Ingredient SALT = new Ingredient("salt", 0, 0.8F).setSellValue(ItemIngredients.Ingredient.SALT.getCost()).setEatTime(0);
    public static final Ingredient SUGAR = new Ingredient("sugar", 2, 0.1F).setEatTime(0);

    //Meals
    public static final Ingredient BUTTER = new Ingredient("butter", 1, 0.5F).setEatTime(-8);
    public static final Ingredient BOILED_EGG = new Ingredient("boiled_egg", 6, 1.14F);
    public static final Ingredient SASHIMI = new Ingredient("sashimi", 6, 1.26F);
    public static final Ingredient SCRAMBLED_EGG = new Ingredient("scrambled_egg", 11, 2.28F);
    public static final Ingredient COOKIES = new Ingredient("cookies", 4, 0.87F);
    public static final Ingredient KETCHUP = new Ingredient("ketchup", 1, 0.2F).setEatTime(0);
    public static final Ingredient NOODLES = new Ingredient("noodles", 13, 2.27F).setEatTime(24);
    public static final Ingredient JAM = new Ingredient("jam", 2, 0.6F).setEatTime(0);
    public static final Ingredient TEMPURA = new Ingredient("tempura", 6, 1.14F);

    //Random Stuff
    public static final Ingredient CHOCOLATE = new Ingredient("chocolate", 2, 1.2F);
    public static final Ingredient EGG = new Ingredient("egg", 3, 0.8F);
    public static final Ingredient FLOUR = new Ingredient("flour", 1, 0.6F).setSellValue(ItemIngredients.Ingredient.FLOUR.getCost());
    public static final Ingredient OIL = new Ingredient("oil", 0, 0.2F).setSellValue(ItemIngredients.Ingredient.OIL.getCost()).setFluid(MappingEvent.OIL).setEatTime(-8);
    public static final Ingredient RICEBALL = new Ingredient("riceball", 1, 0.2F).setSellValue(ItemIngredients.Ingredient.RICEBALL.getCost());
    public static final Ingredient CURRY_POWDER = new Ingredient("curry_powder", 1, 0.2F).setEatTime(-4).setSellValue(ItemIngredients.Ingredient.CURRY_POWDER.getCost());
    public static final Ingredient DUMPLING_POWDER = new Ingredient("dumpling_powder", 1, 0.2F).setEatTime(8).setSellValue(ItemIngredients.Ingredient.DUMPLING_POWDER.getCost());
    public static final Ingredient WINE = new Ingredient("wine", 0, 0.8F).setSellValue(ItemIngredients.Ingredient.WINE.getCost()).setFluid(MappingEvent.WINE).setEatTime(-8);

    public static final Ingredient MILK = new Ingredient("milk", 2, 0.6F).setFluid(MappingEvent.MILK);
    public static final Ingredient MAYONNAISE = new Ingredient("mayonnaise", 3, 1.0F);
    public static final Ingredient BREAD = new Ingredient("bread", 5, 1.2F);
    public static final Ingredient RED_MUSHROOM = new Ingredient("red_mushroom", 4, 0.5F);
    public static final Ingredient BROWN_MUSHROOM = new Ingredient("brown_mushroom", 4, 0.6F);
    public static final Ingredient BAKED_POTATO = new Ingredient("baked_potato", 5, 0.8F);

    //Gathered
    public static final Ingredient BAMBOO = new Ingredient("bamboo", 3, 1F);
    public static final Ingredient MATSUTAKE = new Ingredient("matsutake", 4, 0.6F);

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
    public static final Ingredient APPLE = HFCrops.APPLE.getIngredient();
    public static final Ingredient BANANA = HFCrops.BANANA.getIngredient();
    public static final Ingredient GRAPE = HFCrops.GRAPE.getIngredient();
    public static final Ingredient ORANGE = HFCrops.ORANGE.getIngredient();
    public static final Ingredient PEACH = HFCrops.PEACH.getIngredient();

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
        MUSHROOM.add(RED_MUSHROOM, BROWN_MUSHROOM, MATSUTAKE);
        VEGETABLE_JUICE_BASE.add(TURNIP, CUCUMBER, CABBAGE, TOMATO, ONION, CARROT, SPINACH, GREEN_PEPPER);
        FRUIT_JUICE_BASE.add(STRAWBERRY, WATERMELON);
        SALAD_BASE.add(CUCUMBER, CARROT, TOMATO, CABBAGE, BROWN_MUSHROOM);
        SANDWICH_BASE.add(CUCUMBER, TOMATO, MAYONNAISE, BROWN_MUSHROOM, BOILED_EGG);
        FRUITS.add(ORANGE, BANANA, APPLE, STRAWBERRY, PEACH, PINEAPPLE, WATERMELON);
        CAKE_FRUIT.add(ORANGE, PINEAPPLE, STRAWBERRY, PEACH, GRAPE);
        SASHIMI_VEGETABLE.add(CUCUMBER, TOMATO, ONION, EGGPLANT);
        MEAT.add(CHICKEN, PORK, BEEF, MUTTON, RABBIT);
        FISH.add(SALMON, COD);
    }

    private static String getPrimaryCropName(ItemStack stack) {
        String[] names = InventoryHelper.getOreNames(stack);
        for (String name: names) {
            if (name.startsWith("crop")) return name;
        }

        return null;
    }

    public static void postInit() {
        //Animal Products
        CookingAPI.INSTANCE.register(new ItemStack(Items.EGG), EGG);
        CookingAPI.INSTANCE.register(new ItemStack(Items.MILK_BUCKET), MILK);
        CookingAPI.INSTANCE.register(HFCore.SIZEABLE.getStack(Sizeable.EGG, Size.SMALL), EGG);
        CookingAPI.INSTANCE.register(HFCore.SIZEABLE.getStack(Sizeable.MILK, Size.SMALL), MILK);
        CookingAPI.INSTANCE.register(HFCore.SIZEABLE.getStack(Sizeable.MAYONNAISE, Size.SMALL), MAYONNAISE);

        //Crops
        for (Crop crop: Crop.REGISTRY) {
            if (crop != Crop.NULL_CROP && crop.getIngredient() != null) {
                ItemStack stack = crop.getCropStack(1);
                String name = getPrimaryCropName(stack);
                if (name != null) {
                    registerForOre(name, crop.getIngredient());
                } else CookingAPI.INSTANCE.register(stack, crop.getIngredient());
            }
        }

        //Fruits
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.MELON_BLOCK), WATERMELON);

        //Fungus
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.BROWN_MUSHROOM), BROWN_MUSHROOM);
        CookingAPI.INSTANCE.register(new ItemStack(Blocks.RED_MUSHROOM), RED_MUSHROOM);
        CookingAPI.INSTANCE.register(HFGathering.NATURE.getStackFromEnum(NaturalBlock.MATSUTAKE), MATSUTAKE);

        //Other Plants
        CookingAPI.INSTANCE.register(HFGathering.NATURE.getStackFromEnum(NaturalBlock.BAMBOO), BAMBOO);

        //Ingredients
        registerForOre("foodChocolatebar", CHOCOLATE);
        registerForOre("foodFlour", FLOUR);
        registerForOre("foodOliveoil", OIL);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.RICEBALL), RICEBALL);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.CURRY_POWDER), CURRY_POWDER);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.DUMPLING_POWDER), DUMPLING_POWDER);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.WINE), WINE);

        //Meals - Real
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("butter"), BUTTER);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("egg_boiled"), BOILED_EGG);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("sashimi"), SASHIMI);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("egg_scrambled"), SCRAMBLED_EGG);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("cookies"), COOKIES);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("ketchup"), KETCHUP);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("noodles"), NOODLES);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("jam_strawberry"), JAM);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("jam_apple"), JAM);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("jam_grape"), JAM);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("marmalade"), JAM);
        CookingAPI.INSTANCE.register(CookingAPI.INSTANCE.getBestMeal("tempura"), TEMPURA);

        //Meals - Alts
        registerForOre("foodButter", BUTTER);
        registerForOre("foodScrambledegg", SCRAMBLED_EGG);
        registerForOre("foodKetchup", KETCHUP);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.SASHIMI), SASHIMI);
        CookingAPI.INSTANCE.register(HFCooking.INGREDIENTS.getStackFromEnum(ItemIngredients.Ingredient.COOKIES), COOKIES);

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
        registerForOre("foodSalt", SALT);
        CookingAPI.INSTANCE.register(new ItemStack(Items.SUGAR), SUGAR);
    }

    private static void registerForOre(String ore, Ingredient ingredient) {
        for (ItemStack item: OreDictionary.getOres(ore)) {
            CookingAPI.INSTANCE.register(item, ingredient);
        }
    }
}