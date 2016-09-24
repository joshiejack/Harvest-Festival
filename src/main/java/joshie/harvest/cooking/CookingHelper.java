package joshie.harvest.cooking;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.cooking.item.ItemCookbook;
import joshie.harvest.cooking.recipe.MealImpl;
import joshie.harvest.cooking.tile.TileCooking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.cooking.CookingHelper.PlaceIngredientResult.*;
import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CookingHelper {
    public static ItemStack getRecipe(String name) {
        MealImpl recipe = CookingAPI.REGISTRY.getValue(new ResourceLocation(MODID, name));
        return HFCooking.RECIPE.getStackFromObject(recipe);
    }

    public static boolean hasIngredientInInventory(Set<Ingredient> ingredients, Ingredient ingredient) {
        for (Ingredient inInventory: ingredients) {
            if (ingredient.isEqual(inInventory)) return true;
        }

        return false;
    }

    public static boolean hasAllIngredients(MealImpl recipe, Set<Ingredient> ingredients) {
        for (Ingredient ingredient: recipe.getRequiredIngredients()) {
            if (!hasIngredientInInventory(ingredients, ingredient)) return false;
        }

        return true;
    }

    public static boolean hasAllIngredients(MealImpl recipe, EntityPlayer player) {
        Set<Ingredient> ingredients = new HashSet<>();
        for (ItemStack stack: player.inventory.mainInventory) {
            if (stack != null) {
                ingredients.addAll(CookingAPI.INSTANCE.getCookingComponents(stack));
            }
        }

        ingredients.remove(null); //Remove any nulls
        return hasAllIngredients(recipe, ingredients);
    }

    public static PlaceIngredientResult tryPlaceIngredients(EntityPlayer player, MealImpl recipe) {
        World world = player.worldObj;
        BlockPos pos = new BlockPos(player);
        int reach = player.capabilities.isCreativeMode ? 5 : 4;
        for (int x = -reach; x <= reach; x++) {
            for (int z = -reach; z <= reach; z++) {
                for (int y = -1; y <= 1; y++) {
                    TileEntity tile = world.getTileEntity(pos.add(x, y, z));
                    if (tile instanceof TileCooking) {
                        TileCooking cooking = (TileCooking) tile;
                        PlaceIngredientResult result = cooking.hasPrerequisites();
                        if (result != SUCCESS) return result;
                        if (cooking.getUtensil() == recipe.getRequiredTool() && cooking.getIngredients().size() == 0) {
                            if (ItemCookbook.cook(cooking, player, recipe)) return SUCCESS;
                        }
                    }
                }
            }
        }

        return FAILURE;
    }

    public enum PlaceIngredientResult {
        SUCCESS, FAILURE, MISSING_OVEN, MISSING_COUNTER
    }
}
