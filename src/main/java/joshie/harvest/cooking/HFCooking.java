package joshie.harvest.cooking;

import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.block.BlockCookware;
import joshie.harvest.cooking.item.*;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.cooking.render.*;
import joshie.harvest.cooking.tile.*;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.HFCrops;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import static joshie.harvest.cooking.item.ItemUtensil.Utensil.BLADE;
import static joshie.harvest.cooking.item.ItemUtensil.Utensil.KNIFE;
import static joshie.harvest.cooking.tile.TileMixer.BLADE_STACK;
import static joshie.harvest.core.helpers.ConfigHelper.getDouble;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.helpers.RegistryHelper.registerTiles;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFCOOKING;

@HFLoader(priority = HFCOOKING)
public class HFCooking {
    //Utensils
    public static final Utensil COUNTER = new Utensil(new ResourceLocation(MODID, "counter"));
    public static final Utensil POT = new Utensil(new ResourceLocation(MODID, "pot"));
    public static final Utensil FRYING_PAN = new Utensil(new ResourceLocation(MODID, "frying_pan"));
    public static final Utensil MIXER = new Utensil(new ResourceLocation(MODID, "mixer"));
    public static final Utensil OVEN = new Utensil(new ResourceLocation(MODID, "oven"));

    //Cooking
    public static final BlockCookware COOKWARE = new BlockCookware().register("cookware");
    public static final ItemMeal MEAL = new ItemMeal().register("meal");
    public static final ItemRecipe RECIPE = new ItemRecipe().register("recipe");
    public static final ItemCookbook COOKBOOK = new ItemCookbook().register("cookbook");
    public static final ItemUtensil UTENSILS = new ItemUtensil().register("utensils");
    public static final ItemIngredients INGREDIENTS = new ItemIngredients().register("ingredients");

    @SuppressWarnings("unchecked")
    public static void preInit() {
        BLADE_STACK = UTENSILS.getStackFromEnum(BLADE);
        HFApi.cooking.registerCookingHandler(new RecipeMaker());
        HFApi.cooking.registerKnife(new ItemStack(UTENSILS, 1, KNIFE.ordinal()));
        long bakedPotato = (long) (HFCrops.POTATO.getSellValue() * COOKING_SELL_MODIFIER);
        long cookedRabbit = (long) (40 * COOKING_SELL_MODIFIER);
        long brownMushroom = 30L;
        long redMushroom = 40L;
        HFApi.shipping.registerSellable(new ItemStack(Items.BAKED_POTATO), bakedPotato);
        HFApi.shipping.registerSellable(new ItemStack(Items.BREAD), (long) (Ingredient.FLOUR.getCost() * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.PUMPKIN_PIE), (long) ((10 + HFCrops.PUMPKIN.getSellValue() + Sizeable.EGG.getSmall()) * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.COOKIE), (long) ((Ingredient.FLOUR.getCost() + Ingredient.CHOCOLATE.getCost()) * COOKING_SELL_MODIFIER) / 4);
        HFApi.shipping.registerSellable(new ItemStack(Items.CAKE), (long) ((10 + Ingredient.FLOUR.getCost() +  Sizeable.MILK.getSmall() + Sizeable.EGG.getSmall()) * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.BEETROOT_SOUP), (long) ((10 + Ingredient.OIL.getCost() +  HFCrops.BEETROOT.getSellValue() + HFCrops.ONION.getSellValue() + HFCrops.TOMATO.getSellValue()) * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.RABBIT_STEW), (long) ((bakedPotato +  HFCrops.CARROT.getSellValue() + cookedRabbit + brownMushroom) * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.MUSHROOM_STEW), (long) ((brownMushroom + redMushroom) * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.BEEF), 60);
        HFApi.shipping.registerSellable(new ItemStack(Items.COOKED_BEEF), (long) (60 * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.PORKCHOP), 60);
        HFApi.shipping.registerSellable(new ItemStack(Items.COOKED_PORKCHOP), (long) (60 * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.CHICKEN), 40);
        HFApi.shipping.registerSellable(new ItemStack(Items.COOKED_CHICKEN), (long) (40 * COOKING_SELL_MODIFIER));
        HFApi.shipping.registerSellable(new ItemStack(Items.RABBIT), 40);
        HFApi.shipping.registerSellable(new ItemStack(Items.COOKED_RABBIT), cookedRabbit);
        HFApi.shipping.registerSellable(new ItemStack(Items.MUTTON), 80);
        HFApi.shipping.registerSellable(new ItemStack(Items.COOKED_MUTTON), (long) (80 * COOKING_SELL_MODIFIER));
        OreDictionary.registerOre("foodOliveoil", INGREDIENTS.getStackFromEnum(Ingredient.OIL));
        OreDictionary.registerOre("foodChocolatebar", INGREDIENTS.getStackFromEnum(Ingredient.CHOCOLATE));
        OreDictionary.registerOre("foodFlour", INGREDIENTS.getStackFromEnum(Ingredient.FLOUR));
        OreDictionary.registerOre("foodSalt", INGREDIENTS.getStackFromEnum(Ingredient.SALT));
        OreDictionary.registerOre("foodKetchup", MEAL.getStackFromEnum(Meal.KETCHUP));
        OreDictionary.registerOre("foodButter", MEAL.getStackFromEnum(Meal.BUTTER));
        OreDictionary.registerOre("foodScrambledegg", MEAL.getStackFromEnum(Meal.EGG_SCRAMBLED));
        registerSounds("counter", "fridge", "frying_pan", "mixer", "oven", "oven_done", "oven_door", "pot", "recipe");
        registerTiles(TileFridge.class, TileFryingPan.class, TileCounter.class, TileMixer.class, TileOven.class, TilePot.class);
        COUNTER.setBurntItem(MEAL.getStackFromEnum(Meal.BURNT_COUNTER));
        POT.setBurntItem(MEAL.getStackFromEnum(Meal.BURNT_POT));
        FRYING_PAN.setBurntItem(MEAL.getStackFromEnum(Meal.BURNT_FRYING_PAN));
        MIXER.setBurntItem(MEAL.getStackFromEnum(Meal.BURNT_MIXER));
        OVEN.setBurntItem(MEAL.getStackFromEnum(Meal.BURNT_OVEN));
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(MEAL, new MealDefinition());
        ModelLoader.setCustomMeshDefinition(RECIPE, new MeshIdentical(RECIPE));
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new SpecialRendererPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCounter.class, new SpecialRendererCounter());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMixer.class, new SpecialRendererMixer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileOven.class, new SpecialRendererOven());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFridge.class, new SpecialRendererFridge());
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(RECIPE, COOKBOOK, UTENSILS);
    }

    public static double COOKING_SELL_MODIFIER;

    public static void configure() {
        COOKING_SELL_MODIFIER = getDouble("Cooked Meals Sell Multiplier", 1.12D);
    }
}