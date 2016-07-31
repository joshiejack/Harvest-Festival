package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.blocks.*;
import joshie.harvest.cooking.items.ItemIngredients;
import joshie.harvest.cooking.items.ItemMeal;
import joshie.harvest.cooking.items.ItemUtensil;
import joshie.harvest.cooking.render.*;
import joshie.harvest.core.helpers.ModelHelper;
import joshie.harvest.core.util.HFLoader;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.cooking.blocks.TileMixer.BLADE_STACK;
import static joshie.harvest.cooking.items.ItemUtensil.Utensil.BLADE;
import static joshie.harvest.core.helpers.generic.RegistryHelper.registerTiles;
import static joshie.harvest.core.lib.LoadOrder.HFCOOKING;

@HFLoader(priority = HFCOOKING)
public class HFCooking {
    //Cooking
    public static final BlockCookware COOKWARE = new BlockCookware().register("cookware");
    public static final ItemMeal MEAL = new ItemMeal().register("meal");
    public static final ItemUtensil UTENSILS = new ItemUtensil().register("utensils");
    public static final ItemIngredients INGREDIENTS = new ItemIngredients().register("ingredients");

    public static void preInit() {
        BLADE_STACK = UTENSILS.getStackFromEnum(BLADE);
        HFApi.cooking.registerRecipeHandler(new MayoRecipeHandler());
        registerTiles(TileFridge.class, TileFryingPan.class, TileCounter.class, TileMixer.class, TileOven.class, TilePot.class);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        ModelLoader.setCustomMeshDefinition(MEAL, new MealDefinition());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFryingPan.class, new SpecialRendererFryingPan());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new SpecialRendererPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCounter.class, new SpecialRendererCounter());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMixer.class, new SpecialRendererMixer());
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        for (Recipe recipe : FoodRegistry.REGISTRY.getValues()) {
            ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation(recipe.getRegistryName().getResourceDomain(), "meals/" + recipe.getRegistryName().getResourcePath()), "inventory");
            ModelBakery.registerItemVariants(MEAL, model);
            MealDefinition.registerMeal(recipe, model);
        }

        for (Utensil utensil: Utensil.values()) {
            ModelResourceLocation model = ModelHelper.getModelForItem("meals/burnt" + utensil.name());
            ModelBakery.registerItemVariants(MEAL, model);
            MealDefinition.registerBurnt(utensil.ordinal(), model);
        }
    }
}