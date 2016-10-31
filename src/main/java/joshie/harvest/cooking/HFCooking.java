package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.block.BlockCookware;
import joshie.harvest.cooking.item.*;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.cooking.recipe.RecipeMayo;
import joshie.harvest.cooking.render.*;
import joshie.harvest.cooking.tile.*;
import joshie.harvest.core.base.render.FMLDefinition;
import joshie.harvest.core.base.render.MeshIdentical;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import static joshie.harvest.cooking.item.ItemUtensil.Utensil.BLADE;
import static joshie.harvest.cooking.item.ItemUtensil.Utensil.KNIFE;
import static joshie.harvest.cooking.tile.TileMixer.BLADE_STACK;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.helpers.RegistryHelper.registerTiles;
import static joshie.harvest.core.lib.LoadOrder.HFCOOKING;

@HFLoader(priority = HFCOOKING)
public class HFCooking {
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
        HFApi.cooking.registerRecipeHandler(new RecipeMayo());
        HFApi.cooking.registerCookingHandler(new RecipeMaker());
        HFApi.cooking.registerKnife(new ItemStack(UTENSILS, 1, KNIFE.ordinal()));
        OreDictionary.registerOre("foodOliveoil", INGREDIENTS.getStackFromEnum(Ingredient.OIL));
        OreDictionary.registerOre("foodChocolatebar", INGREDIENTS.getStackFromEnum(Ingredient.CHOCOLATE));
        OreDictionary.registerOre("foodButter", INGREDIENTS.getStackFromEnum(Ingredient.BUTTER));
        OreDictionary.registerOre("foodFlour", INGREDIENTS.getStackFromEnum(Ingredient.FLOUR));
        OreDictionary.registerOre("foodSalt", INGREDIENTS.getStackFromEnum(Ingredient.SALT));
        OreDictionary.registerOre("foodKetchup", INGREDIENTS.getStackFromEnum(Ingredient.KETCHUP));
        OreDictionary.registerOre("foodScrambledegg", INGREDIENTS.getStackFromEnum(Ingredient.EGG_SCRAMBLED));
        registerSounds("counter", "fridge", "frying_pan", "mixer", "oven", "oven_done", "oven_door", "pot", "recipe");
        registerTiles(TileFridge.class, TileFryingPan.class, TileCounter.class, TileMixer.class, TileOven.class, TilePot.class);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(MEAL, new MealDefinition(MEAL, "meals", Recipe.REGISTRY) {
            @Override
            public boolean shouldSkip(Recipe recipe) {
                return super.shouldSkip(recipe) || !recipe.supportsNBTData();
            }
        });

        ModelLoader.setCustomMeshDefinition(RECIPE, new MeshIdentical(RECIPE));
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new SpecialRendererPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCounter.class, new SpecialRendererCounter());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMixer.class, new SpecialRendererMixer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileOven.class, new SpecialRendererOven());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFridge.class, new SpecialRendererFridge());
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        FMLDefinition.getDefinition("meals").registerEverything();
    }
}