package joshie.harvest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.cooking.blocks.TileCooking;
import joshie.harvest.cooking.items.ItemCookbook;
import joshie.harvest.cooking.recipe.Recipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CookingHelper {
    public static ItemStack getRecipe(String name) {
        Recipe recipe = FoodRegistry.REGISTRY.getObject(new ResourceLocation(MODID, name));
        return HFCooking.RECIPE.getStackFromObject(recipe);
    }

    public static boolean hasIngredientInInventory(Set<ICookingIngredient> ingredients, ICookingIngredient ingredient) {
        for (ICookingIngredient inInventory: ingredients) {
            if (ingredient.isEqual(inInventory)) return true;
        }

        return false;
    }

    public static boolean hasAllIngredients(Recipe recipe, Set<ICookingIngredient> ingredients) {
        for (ICookingIngredient ingredient: recipe.getRequiredIngredients()) {
            if (!hasIngredientInInventory(ingredients, ingredient)) return false;
        }

        return true;
    }

    public static boolean hasAllIngredients(Recipe recipe, EntityPlayer player) {
        Set<ICookingIngredient> ingredients = new HashSet<>();
        for (ItemStack stack: player.inventory.mainInventory) {
            if (stack != null) {
                ingredients.addAll(HFApi.cooking.getCookingComponents(stack));
            }
        }

        ingredients.remove(null); //Remove any nulls
        return hasAllIngredients(recipe, ingredients);
    }

    public static boolean tryPlaceIngredients(EntityPlayer player, Recipe recipe) {
        World world = player.worldObj;
        BlockPos pos = new BlockPos(player);
        int reach = player.capabilities.isCreativeMode ? 5 : 4;
        for (int x = -reach; x <= reach; x++) {
            for (int z = -reach; z <= reach; z++) {
                for (int y = -1; y <= 1; y++) {
                    TileEntity tile = world.getTileEntity(pos.add(x, y, z));
                    if (tile instanceof TileCooking) {
                        TileCooking cooking = (TileCooking) tile;
                        if (cooking.getUtensil() == recipe.getRequiredTool() && cooking.getIngredients().size() == 0) {
                            if (ItemCookbook.cook(cooking, player, recipe)) return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
