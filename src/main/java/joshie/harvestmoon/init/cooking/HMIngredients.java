package joshie.harvestmoon.init.cooking;

import static joshie.harvestmoon.cooking.FoodRegistry.register;
import joshie.harvestmoon.cooking.Ingredient;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemGeneral;
import joshie.harvestmoon.lib.CropMeta;
import joshie.harvestmoon.lib.SizeableMeta;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class HMIngredients {
    /** Created **/
    public static Ingredient potato_slices;
    public static Ingredient butter;
    public static Ingredient whisked_egg;
    
    /** Other stuff **/
    public static Ingredient bamboo_shoot;
    public static Ingredient egg;
    public static Ingredient fish;
    public static Ingredient flour;
    public static Ingredient matsutake_mushroom;
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

    public static void init() {
        addFoodCategories();
        addIngredients();
        assignIngredients();
    }
    
    private static void addFoodCategories() {
        mushroom = new Ingredient("mushroom", 2, -1, 1, 0.02F, 8);
    }

    private static void addIngredients() {
        potato_slices = new Ingredient("potato_slices", 3, -2, 1, 0.05F, 4);
        butter = new Ingredient("butter", 6, -1, 0, 0.01F, 6);
        whisked_egg = new Ingredient("egg_whisked", 5, 0, 0, 0.07F, 4);
        
        bamboo_shoot = new Ingredient("shoot_bamboo", 2, -1, 1, 0.025F, 4);
        egg = new Ingredient("egg", 6, 0, 0, 0.075F, 10);   
        fish = new Ingredient("fish", 5, -5, 2, 0.1F, 16);
        flour = new Ingredient("flour", 0, 0, 0, 0.1F, 4);
        matsutake_mushroom = new Ingredient("mushroom_matustake", 3, -1, 1, 0.025F, 10).assign(mushroom);
        oil = new Ingredient("oil", 0, -2, 0, 0F, 2);
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
    }

    private static void assignIngredients() {
        register(new ItemStack(HMItems.general, 1, ItemGeneral.POTATO_SLICES), potato_slices);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.BUTTER), butter);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.WHISKED_EGG), whisked_egg);
        
        register(new ItemStack(HMItems.general, 1, ItemGeneral.BAMBOO_SHOOT), bamboo_shoot);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.FLOUR), flour);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.OIL), oil);
        register(new ItemStack(HMItems.general, 1, ItemGeneral.RICEBALL), riceball);
        
        register(new ItemStack(HMItems.crops, 1, CropMeta.TURNIP.ordinal()), turnip);
        register(new ItemStack(HMItems.crops, 1, CropMeta.POTATO.ordinal()), potato);
        register(new ItemStack(Items.potato, 1, OreDictionary.WILDCARD_VALUE), potato);
        register(new ItemStack(HMItems.crops, 1, CropMeta.CUCUMBER.ordinal()), cucumber);
        register(new ItemStack(HMItems.crops, 1, CropMeta.STRAWBERRY.ordinal()), strawberry);
        register(new ItemStack(HMItems.crops, 1, CropMeta.CABBAGE.ordinal()), cabbage);
        register(new ItemStack(HMItems.crops, 1, CropMeta.TOMATO.ordinal()), tomato);
        register(new ItemStack(HMItems.crops, 1, CropMeta.ONION.ordinal()), onion);
        register(new ItemStack(HMItems.crops, 1, CropMeta.CORN.ordinal()), corn);
        register(new ItemStack(HMItems.crops, 1, CropMeta.PUMPKIN.ordinal()), pumpkin);
        register(new ItemStack(Blocks.pumpkin, 1, OreDictionary.WILDCARD_VALUE), pumpkin);
        register(new ItemStack(HMItems.crops, 1, CropMeta.PINEAPPLE.ordinal()), pineapple);
        register(new ItemStack(HMItems.crops, 1, CropMeta.EGGPLANT.ordinal()), eggplant);
        register(new ItemStack(HMItems.crops, 1, CropMeta.CARROT.ordinal()), carrot);
        register(new ItemStack(Items.carrot, 1, OreDictionary.WILDCARD_VALUE), carrot);
        register(new ItemStack(HMItems.crops, 1, CropMeta.SWEET_POTATO.ordinal()), sweet_potato);
        register(new ItemStack(HMItems.crops, 1, CropMeta.SPINACH.ordinal()), spinach);
        register(new ItemStack(HMItems.crops, 1, CropMeta.GREEN_PEPPER.ordinal()), green_pepper);
        
        register(new ItemStack(Items.wheat, 1, OreDictionary.WILDCARD_VALUE), wheat);
        register(new ItemStack(Items.melon, 1, OreDictionary.WILDCARD_VALUE), watermelon);
        register(new ItemStack(Blocks.melon_block, 1, OreDictionary.WILDCARD_VALUE), watermelon);

        register(new ItemStack(HMItems.sized, 1, SizeableMeta.EGG.ordinal()), egg);
        register(new ItemStack(Items.egg, 1, OreDictionary.WILDCARD_VALUE), egg);
        register(new ItemStack(Items.fish, 1, OreDictionary.WILDCARD_VALUE), fish);
        register(new ItemStack(HMItems.sized, 1, SizeableMeta.MILK.ordinal()), milk);
        register(new ItemStack(Items.milk_bucket, 1, OreDictionary.WILDCARD_VALUE), milk);
        register(new ItemStack(HMItems.sized, 1, SizeableMeta.MAYONNAISE.ordinal()), mayonnaise);
        register(new ItemStack(HMItems.sized, 1, SizeableMeta.MATSUTAKE.ordinal()), matsutake_mushroom);
        
        register(new ItemStack(Blocks.brown_mushroom, 1, OreDictionary.WILDCARD_VALUE), mushroom);
        register(new ItemStack(Blocks.red_mushroom, 1, OreDictionary.WILDCARD_VALUE), mushroom);
    }
}
