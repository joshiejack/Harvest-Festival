package joshie.harvestmoon.init.cooking;

import static joshie.harvestmoon.cooking.FoodRegistry.register;
import joshie.harvestmoon.cooking.Ingredient;
import joshie.harvestmoon.core.lib.SizeableMeta;
import joshie.harvestmoon.init.HMCooking;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class HMIngredients {
    /** Seasonings **/
    public static Ingredient salt;
    public static Ingredient sugar;
    
    /** Created **/
    public static Ingredient potato_slices;
    public static Ingredient butter;
    public static Ingredient whisked_egg;
    
    /** Other stuff **/
    public static Ingredient egg;
    public static Ingredient fish;
    public static Ingredient flour;
    public static Ingredient oil;
    public static Ingredient riceball;
    public static Ingredient milk;
    public static Ingredient mayonnaise;
    public static Ingredient mushroom;

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
    public static Ingredient bread;

    public static void init() {
        addFoodCategories();
        addIngredients();
        assignIngredients();
    }
    
    private static void addFoodCategories() {
        mushroom = new Ingredient("mushroom", 2, -1, 1, 0.02F, 8);
    }

    private static void addIngredients() {
        salt = new Ingredient("salt", 0, 0, 0, 0.01F, 0);
        sugar = new Ingredient("sugar", 1, 0, 0, 0F, 0);
        
        potato_slices = new Ingredient("potato_slices", 3, -2, 1, 0.05F, 4);
        butter = new Ingredient("butter", 6, -1, 0, 0.01F, 6);
        whisked_egg = new Ingredient("egg_whisked", 5, 0, 0, 0.07F, 4);
        
        egg = new Ingredient("egg", 6, 0, 0, 0.075F, 10);   
        fish = new Ingredient("fish", 5, -5, 2, 0.1F, 16);
        flour = new Ingredient("flour", 0, 0, 0, 0.1F, 4);
        oil = new Ingredient("oil", 0, -2, 0, 0F, 2).setFluid(HMCooking.cookingOil);
        riceball = new Ingredient("riceball", 6, -3, 1, 0.085F, 16); 
        mayonnaise = new Ingredient("mayonnaise", 6, -1, 1, 0.8F, 8);
        milk = new Ingredient("milk", 5, -2, 0, 0.04F, 6);
        
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
    }

    private static void assignIngredients() {
        register(new ItemStack(HMItems.general, 1, ItemGeneral.SALT), salt);
        register(new ItemStack(Items.sugar, 1, OreDictionary.WILDCARD_VALUE), sugar);
        
        register(new ItemStack(HMItems.general, 1, ItemGeneral.POTATO_SLICES), potato_slices);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.BUTTER), butter);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.WHISKED_EGG), whisked_egg);
        
        register(new ItemStack(HMItems.general, 1, ItemGeneral.FLOUR), flour);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.OIL), oil);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.RICEBALL), riceball);
        
        register(HMCrops.turnip.getCropStack(), turnip);
        register(HMCrops.potato.getCropStack(), potato);
        register(new ItemStack(Items.potato, 1, OreDictionary.WILDCARD_VALUE), potato);
        register(HMCrops.cucumber.getCropStack(), cucumber);
        register(HMCrops.strawberry.getCropStack(), strawberry);
        register(HMCrops.cabbage.getCropStack(), cabbage);
        register(HMCrops.tomato.getCropStack(), tomato);
        register(HMCrops.onion.getCropStack(), onion);
        register(HMCrops.corn.getCropStack(), corn);
        register(HMCrops.pumpkin.getCropStack(), pumpkin);
        register(new ItemStack(Blocks.pumpkin, 1, OreDictionary.WILDCARD_VALUE), pumpkin);
        register(HMCrops.pineapple.getCropStack(), pineapple);
        register(HMCrops.eggplant.getCropStack(), eggplant);
        register(HMCrops.carrot.getCropStack(), carrot);
        register(new ItemStack(Items.carrot, 1, OreDictionary.WILDCARD_VALUE), carrot);
        register(HMCrops.sweet_potato.getCropStack(), sweet_potato);
        register(HMCrops.spinach.getCropStack(), spinach);
        register(HMCrops.green_pepper.getCropStack(), green_pepper);
        
        register(new ItemStack(Items.wheat, 1, OreDictionary.WILDCARD_VALUE), wheat);
        register(new ItemStack(Items.melon, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        register(new ItemStack(Blocks.melon_block, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        register(new ItemStack(Items.bread, 1, OreDictionary.WILDCARD_VALUE), bread);
        
        register(new ItemStack(HMItems.sized, 1, SizeableMeta.EGG.ordinal()), egg);
        register(new ItemStack(Items.egg, 1, OreDictionary.WILDCARD_VALUE), egg);
        register(new ItemStack(Items.fish, 1, OreDictionary.WILDCARD_VALUE), fish);
        register(new ItemStack(HMItems.sized, 1, SizeableMeta.MILK.ordinal()), milk);
        register(new ItemStack(Items.milk_bucket, 1, OreDictionary.WILDCARD_VALUE), milk);
        register(new ItemStack(HMItems.sized, 1, SizeableMeta.MAYONNAISE.ordinal()), mayonnaise);
        
        register(new ItemStack(Blocks.brown_mushroom, 1, OreDictionary.WILDCARD_VALUE), mushroom);
        register(new ItemStack(Blocks.red_mushroom, 1, OreDictionary.WILDCARD_VALUE), mushroom);
    }
}
