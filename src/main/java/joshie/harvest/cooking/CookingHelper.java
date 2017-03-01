package joshie.harvest.cooking;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.item.ItemCookbook;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.cooking.tile.TileCooking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.cooking.CookingHelper.PlaceIngredientResult.FAILURE;
import static joshie.harvest.cooking.CookingHelper.PlaceIngredientResult.SUCCESS;
import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CookingHelper {
    public static ItemStack getRecipe(String name) {
        return HFCooking.RECIPE.getStackFromObject(Recipe.REGISTRY.get(new ResourceLocation(MODID, name)));
    }

    public static boolean hasAllIngredients(Recipe recipe, EntityPlayer player) {
        Set<IngredientStack> ingredients = new HashSet<>();
        for (ItemStack stack: player.inventory.mainInventory) {
            if (stack != null) {
                Ingredient ingredient = CookingAPI.INSTANCE.getCookingComponents(stack);
                if (ingredient != null) {
                    ingredients.add(new IngredientStack(ingredient, stack.stackSize));
                }
            }
        }

        ingredients.remove(null); //Remove any nulls
        return RecipeMaker.areAllRequiredInRecipe(recipe.getRequired(), ingredients);
    }

    public static PlaceIngredientResult tryPlaceIngredients(EntityPlayer player, Recipe recipe) {
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
                        if (cooking.getUtensil() == recipe.getUtensil() && cooking.getIngredients().size() == 0) {
                            if (ItemCookbook.cook(cooking, player, recipe)) return SUCCESS;
                        }
                    }
                }
            }
        }

        return FAILURE;
    }

    public static ItemStack makeRecipe(Recipe recipe) {
        return RecipeMaker.BUILDER.build(recipe, new ArrayList<>(recipe.getRequired())).get(0);
    }

    public enum PlaceIngredientResult {
        SUCCESS, FAILURE, MISSING_OVEN, MISSING_COUNTER
    }
}
