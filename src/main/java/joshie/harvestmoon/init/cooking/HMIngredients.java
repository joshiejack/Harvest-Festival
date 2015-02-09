package joshie.harvestmoon.init.cooking;

import static joshie.harvestmoon.cooking.FoodRegistry.register;
import joshie.harvestmoon.cooking.Ingredient;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.lib.CropMeta;
import joshie.harvestmoon.lib.SizeableMeta;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class HMIngredients {
    /** Other stuff **/
    public static Ingredient bamboo_shoot;
    public static Ingredient egg;
    public static Ingredient fish;
    public static Ingredient flour;
    public static Ingredient matsutake_mushroom;
    public static Ingredient oil;
    public static Ingredient riceball;

    /** Crops **/
    public static Ingredient turnip;
    public static Ingredient potato;
    public static Ingredient cucumber;
    public static Ingredient strawberry;
    public static Ingredient cabbage;
    public static Ingredient tomato;
    public static Ingredient onion;
    public static Ingredient corn;
    public static Ingredient pumpkin;
    public static Ingredient pineapple;
    public static Ingredient eggplant;
    public static Ingredient carrot;
    public static Ingredient sweet_potato;
    public static Ingredient spinach;
    public static Ingredient green_pepper;

    public static Ingredient watermelon;
    public static Ingredient wheat;

    public static void init() {
        addIngredients();
        assignIngredients();
    }

    private static void addIngredients() {
        bamboo_shoot = new Ingredient(2, -1, 1, 0.025F, 4);
        egg = new Ingredient(6, 0, 0, 0.075F, 10);   
        fish = new Ingredient(5, -5, 2, 0.1F, 16);
        flour = new Ingredient(0, 0, 0, 0.1F, 4);
        matsutake_mushroom = new Ingredient(3, -1, 1, 0.025F, 10);
        oil = new Ingredient(0, -2, 0, 0F, 2);
        riceball = new Ingredient(6, -3, 1, 0.085F, 16); 
        
        //TODO: Stats for the ingredients I added.
        turnip = new Ingredient(2, -1, 1, 0.033F, 8);
        potato = new Ingredient(5, -1, 1, 0.08F, 16);
        cucumber = new Ingredient(3, -1, 1, 0.02F, 4);
        strawberry = new Ingredient(3, -2, 1, 0.025F, 8);
        cabbage = new Ingredient(3, -3, 1, 0.05F, 8);
        tomato = new Ingredient(2, -1, 1, 0.035F, 10);
        onion = new Ingredient(2, -2, 1, 0.03F, 8);
        corn = new Ingredient(5, -3, 1, 0.0375F, 16);
        pumpkin = new Ingredient(4, -1, 2, 0.06F, 16);
        pineapple = new Ingredient(3, -3, 1, 0.055F, 16);
        eggplant = new Ingredient(2, -3, 1, 0.05F, 8);
        carrot = new Ingredient(2, -2, 1, 0.035F, 6);
        sweet_potato = new Ingredient(6, -2, 1, 0.04F, 16);
        spinach = new Ingredient(3, -4, 1, 0.022F, 8);
        green_pepper = new Ingredient(4, -2, 1, 0.03F, 12);
        watermelon = new Ingredient(1, -1, 1, 0.05F, 4);
        wheat = new Ingredient(6, -3, 1, 0.0275F, 16);
    }

    private static void assignIngredients() {
        //Turnips
        register(new ItemStack(HMItems.crops, 1, CropMeta.TURNIP.ordinal()), turnip);

        //Potatoes
        register(new ItemStack(HMItems.crops, 1, CropMeta.POTATO.ordinal()), potato);
        register(new ItemStack(Items.potato, 1, OreDictionary.WILDCARD_VALUE), potato);

        //Cucumbers
        register(new ItemStack(HMItems.crops, 1, CropMeta.CUCUMBER.ordinal()), cucumber);

        //Strawberries
        register(new ItemStack(HMItems.crops, 1, CropMeta.STRAWBERRY.ordinal()), strawberry);

        //Cabbages
        register(new ItemStack(HMItems.crops, 1, CropMeta.CABBAGE.ordinal()), cabbage);

        //Tomatoes
        register(new ItemStack(HMItems.crops, 1, CropMeta.TOMATO.ordinal()), tomato);

        //Onions
        register(new ItemStack(HMItems.crops, 1, CropMeta.ONION.ordinal()), onion);

        //Corns
        register(new ItemStack(HMItems.crops, 1, CropMeta.CORN.ordinal()), corn);

        //Pumpkins
        register(new ItemStack(HMItems.crops, 1, CropMeta.PUMPKIN.ordinal()), pumpkin);
        register(new ItemStack(Blocks.pumpkin, 1, OreDictionary.WILDCARD_VALUE), pumpkin);

        //Pineapples
        register(new ItemStack(HMItems.crops, 1, CropMeta.PINEAPPLE.ordinal()), pineapple);

        //Eggplants
        register(new ItemStack(HMItems.crops, 1, CropMeta.EGGPLANT.ordinal()), eggplant);

        //Carrots
        register(new ItemStack(HMItems.crops, 1, CropMeta.CARROT.ordinal()), carrot);
        register(new ItemStack(Items.carrot, 1, OreDictionary.WILDCARD_VALUE), carrot);

        //Sweet Potatoes
        register(new ItemStack(HMItems.crops, 1, CropMeta.SWEET_POTATO.ordinal()), sweet_potato);

        //Spinaches
        register(new ItemStack(HMItems.crops, 1, CropMeta.SPINACH.ordinal()), spinach);

        //Green Peppers
        register(new ItemStack(HMItems.crops, 1, CropMeta.GREEN_PEPPER.ordinal()), green_pepper);

        //Wheats
        register(new ItemStack(Items.wheat, 1, OreDictionary.WILDCARD_VALUE), wheat);

        //Watermelons
        register(new ItemStack(Items.melon, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        register(new ItemStack(Blocks.melon_block, 1, OreDictionary.WILDCARD_VALUE), watermelon);

        //Eggs
        register(new ItemStack(HMItems.sized, 1, SizeableMeta.EGG.ordinal()), egg);
        register(new ItemStack(Items.egg, 1, OreDictionary.WILDCARD_VALUE), egg);

        //Fish
        register(new ItemStack(Items.fish, 1, OreDictionary.WILDCARD_VALUE), fish);
    }
}
