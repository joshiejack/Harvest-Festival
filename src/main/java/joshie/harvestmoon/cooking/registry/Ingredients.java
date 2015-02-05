package joshie.harvestmoon.cooking.registry;

import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.cooking.Ingredient;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.lib.CropMeta;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Ingredients {
    public static Ingredient bamboo_shoot;
    public static Ingredient cabbage;
    public static Ingredient carrot;
    public static Ingredient egg;
    public static Ingredient eggplant;
    public static Ingredient fish;
    public static Ingredient flour;
    public static Ingredient green_pepper;
    public static Ingredient matsutake_mushroom;
    public static Ingredient onion;
    public static Ingredient oil;
    public static Ingredient riceball;

    public static void init() {
        addIngredients();
        assignIngredients();
    }

    private static void addIngredients() {
        bamboo_shoot = new Ingredient("bamboo_shoot", 2, -1, 1, 0.025F, 4);
        cabbage = new Ingredient("cabbage", 3, -3, 1, 0.05F, 8);
        carrot = new Ingredient("carrot", 2, -2, 1, 0.035F, 6);
        egg = new Ingredient("egg", 6, 0, 0, 0.075F, 10);
        eggplant = new Ingredient("eggplant", 2, -3, 1, 0.05F, 8);
        fish = new Ingredient("fish", 5, -5, 2, 0.1F, 16);
        flour = new Ingredient("flour", 0, 0, 0, 0.1F, 4);
        green_pepper = new Ingredient("green_pepper", 4, -2, 1, 0.03F, 12);
        matsutake_mushroom = new Ingredient("matsutake_mushroom", 3, -1, 1, 0.025F, 10);
        onion = new Ingredient("onion", 2, -2, 1, 0.03F, 8);
        oil = new Ingredient("oil", 0, -2, 0, 0F, 2);
        riceball = new Ingredient("riceball", 6, -3, 1, 0.085F, 16);
    }

    private static void assignIngredients() {
        FoodRegistry.register(new ItemStack(Items.carrot, 1, OreDictionary.WILDCARD_VALUE), carrot);
        FoodRegistry.register(new ItemStack(HMItems.crops, 1, CropMeta.CARROT.ordinal()), carrot);
        FoodRegistry.register(new ItemStack(Items.egg, 1, OreDictionary.WILDCARD_VALUE), egg);
        FoodRegistry.register(new ItemStack(HMItems.crops, 1, CropMeta.EGGPLANT.ordinal()), eggplant);
        FoodRegistry.register(new ItemStack(Items.fish, 1, OreDictionary.WILDCARD_VALUE), fish);
        FoodRegistry.register(new ItemStack(HMItems.crops, 1, CropMeta.GREEN_PEPPER.ordinal()), green_pepper);
        FoodRegistry.register(new ItemStack(HMItems.crops, 1, CropMeta.ONION.ordinal()), onion);
    }
}
