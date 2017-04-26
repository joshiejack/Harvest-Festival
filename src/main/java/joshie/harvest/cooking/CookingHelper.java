package joshie.harvest.cooking;

import joshie.harvest.api.cooking.IFridge;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.cooking.tile.TileCooking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static joshie.harvest.cooking.CookingHelper.PlaceIngredientResult.FAILURE;
import static joshie.harvest.cooking.CookingHelper.PlaceIngredientResult.SUCCESS;
import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CookingHelper {
    @Nonnull
    public static ItemStack getRecipe(String name) {
        return HFCooking.RECIPE.getStackFromObject(Recipe.REGISTRY.get(new ResourceLocation(MODID, name)));
    }

    private static void addIngredientsToSet(Set<IngredientStack> ingredients, IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            Ingredient ingredient = CookingAPI.INSTANCE.getCookingComponents(stack);
            if (ingredient != null) {
                ingredients.add(new IngredientStack(ingredient, stack.getCount()));
            }
        }
    }

    private static boolean hasAllIngredients(Recipe recipe, List<IInventory> inventories) {
        Set<IngredientStack> ingredients = new HashSet<>();
        inventories.stream().forEach(inventory -> addIngredientsToSet(ingredients, inventory));
        ingredients.remove(null); //Remove any nulls
        return RecipeMaker.areAllRequiredInRecipe(recipe.getRequired(), ingredients);
    }

    public static List<IInventory> getAllFridges(EntityPlayer player, World world, BlockPos pos, int reach) {
        List<IInventory> fridges = new ArrayList<>();
        fridges.add(player.inventory);
        for (int x = -reach; x <= reach; x++) {
            for (int z = -reach; z <= reach; z++) {
                for (int y = -1; y <= 1; y++) {
                    TileEntity tile = world.getTileEntity(pos.add(x, y, z));
                    if (tile instanceof IFridge) {
                        fridges.add(((IFridge)tile).getContents());
                    }
                }
            }
        }

        return fridges;
    }

    public static PlaceIngredientResult tryPlaceIngredients(EntityPlayer player, Recipe recipe) {
        World world = player.getEntityWorld();
        BlockPos pos = new BlockPos(player);
        int reach = player.capabilities.isCreativeMode ? 5 : 4;
        List<IInventory> fridges = getAllFridges(player, world, pos, reach);
        for (int x = -reach; x <= reach; x++) {
            for (int z = -reach; z <= reach; z++) {
                for (int y = -1; y <= 1; y++) {
                    TileEntity tile = world.getTileEntity(pos.add(x, y, z));
                    if (tile instanceof TileCooking) {
                        TileCooking cooking = (TileCooking) tile;
                        PlaceIngredientResult result = cooking.hasPrerequisites();
                        if (result != SUCCESS) return result;
                        if (cooking.getUtensil() == recipe.getUtensil() && cooking.getIngredients().size() == 0 && cooking.getResult().size() == 0) {
                            if (cook(cooking, recipe, fridges)) return SUCCESS;
                        }
                    }
                }
            }
        }

        return FAILURE;
    }

    @Nonnull
    public static ItemStack makeRecipe(Recipe recipe) {
        return RecipeMaker.BUILDER.build(recipe, new ArrayList<>(recipe.getRequired())).get(0);
    }

    private static boolean isIngredient(IngredientStack ingredient, Ingredient check) {
        return check != null && ingredient.isSame(new IngredientStack(check));
    }

    @Nonnull
    private static ItemStack getAndRemoveIngredient(IngredientStack ingredient, List<IInventory> fridges) {
        for (IInventory inventory: fridges) {
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (isIngredient(ingredient, CookingAPI.INSTANCE.getCookingComponents(stack))) {
                    return inventory.decrStackSize(i, 1);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    private static boolean cook(TileCooking cooking, Recipe selected, List<IInventory> fridges) {
        if (selected != null) {
            if (!hasAllIngredients(selected, fridges)) return false;
            else {
                for (IngredientStack ingredient : selected.getRequired()) {
                    ItemStack ret = getAndRemoveIngredient(ingredient, fridges);
                    if (ret.isEmpty()) return false;
                    else cooking.addIngredient(ret);
                }

                return true;
            }
        } else return false;
    }

    public enum PlaceIngredientResult {
        SUCCESS, FAILURE, MISSING_OVEN, MISSING_COUNTER
    }
}
