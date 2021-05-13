package uk.joshiejack.gastronomy.cooking;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.block.BlockCookware;
import uk.joshiejack.gastronomy.block.GastronomyBlocks;
import uk.joshiejack.gastronomy.item.GastronomyItems;
import uk.joshiejack.gastronomy.tile.base.TileCooking;
import uk.joshiejack.gastronomy.tile.base.TileCookingFluids;
import uk.joshiejack.penguinlib.util.helpers.generic.MapHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TerrainHelper;

import java.util.Collection;
import java.util.List;

import static uk.joshiejack.gastronomy.tile.base.TileCooking.IN_UTENSIL;

public class Cooker {
    public static ItemStack cook(Appliance appliance, NonNullList<ItemStack> stacks, List<FluidStack> fluids) {
        List<IngredientStack> ingredients = Lists.newArrayList();
        Object2IntMap<Ingredient> map = new Object2IntOpenHashMap<>();
        stacks.forEach(stack -> MapHelper.adjustOrPut(map, Ingredient.registry.getValue(stack), 1, 1));
        map.forEach((e, k) -> ingredients.add(new IngredientStack(e, k)));
        fluids.forEach((fluid) -> ingredients.add(new IngredientStack(fluid)));

        ItemStack ret = getResult(Recipe.PRIORITY_RECIPES.get(appliance), ingredients);
        if (ret.isEmpty()) ret = getResult(Recipe.RECIPES.get(appliance), ingredients);
        return ret.isEmpty() ? GastronomyItems.BURNT.getStackFromEnum(appliance) : ret;
    }

    private static ItemStack getResult(Collection<Recipe> recipes, List<IngredientStack> ingredients) {
        for (Recipe recipe: recipes) {
            ItemStack ret = recipe.cook(ingredients);
            if (!ret.isEmpty()) {
                return ret;
            }
        }

        return ItemStack.EMPTY;
    }

    @SuppressWarnings("ConstantConditions")
    public static void removeUtensilTag(ItemStack stack) {
        //Remove the in_utensil tag, and if no tags left, remove all tags entirely
        if (stack.hasTagCompound()) stack.getTagCompound().removeTag(IN_UTENSIL);
        if (stack.hasTagCompound() && stack.getTagCompound().isEmpty()) {
            stack.setTagCompound(null);
        }
    }

    public static TileCooking getNearbyAppliance(EntityPlayer player, Appliance appliance) {
        BlockPos pos = TerrainHelper.getFirstBlockWithinReach(player, (world, target) -> {
            TileEntity tile = world.getTileEntity(target);
            if (tile instanceof TileCooking) {
                TileCooking cooking = (TileCooking) tile;
                IItemHandler handler = cooking.getHandler();
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty()) return false;
                }

                return cooking.appliance == appliance;
            }

            return false;
        });

        return pos != null ? ((TileCooking)player.world.getTileEntity(pos)) : null;
    }

    public static boolean learnRecipe(EntityPlayer player, ItemStack stack) {
        return player.world.isRemote ? RecipeBook.learnRecipe(stack) : RecipeArchives.get(player.world).learnRecipe(player, stack);
    }

    public static List<IItemHandlerModifiable> getFoodStorageAndPlayer(EntityPlayer player) {
        List<IItemHandlerModifiable> inventories = Lists.newArrayList();
        inventories.add(new PlayerInvWrapper(player.inventory));
        inventories.addAll(TerrainHelper.getAllBlocksWithinReach(player, (pos, list) -> {
            TileEntity tile = player.world.getTileEntity(pos);
            if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
                for (Class<? extends TileEntity> c : Gastronomy.FOOD_STORAGE) {
                    if (c.isAssignableFrom(tile.getClass())) {
                        Object o = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
                        if (o instanceof IItemHandlerModifiable) {
                            list.add((IItemHandlerModifiable) o);
                        }
                    }
                }
            }

            return true;
        }));

        return inventories;
    }

    private static IngredientStack getIngredientStackFromItemStack(boolean fluids, ItemStack stack) {
        if (fluids) {
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid != null) {
                return new IngredientStack(fluid);
            }
        }

        Ingredient iIngredient = Ingredient.registry.getValue(stack);
        return new IngredientStack(iIngredient, stack.getCount());
    }

    //TODO: remove items from fridge and cupboard
    public static void takeFromFridgeOrPlayerInventory(TileCooking cooking, List<IItemHandlerModifiable> inventories, List<IngredientStack> ingredients) {
        boolean fluids = cooking instanceof TileCookingFluids;
        for (IngredientStack ingredient: ingredients) {
            int count = ingredient.getAmount();
            handlers:
            for (IItemHandlerModifiable handler: inventories) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    IngredientStack input = getIngredientStackFromItemStack(fluids, stack);
                    if (!input.isEmpty() && input.matches(ingredient)) {
                        if (fluids && input.isFluid()) {
                            EntityPlayer player = FakePlayerHelper.getFakePlayerWithPosition((WorldServer) cooking.getWorld(), cooking.getPos());
                            ((TileCookingFluids)cooking).handleFluids(player, EnumHand.MAIN_HAND, stack);
                            handler.setStackInSlot(i, player.getHeldItem(EnumHand.MAIN_HAND));
                            break handlers; //Really doesn't matter for a fluid
                        } else {
                            ItemStack stackTaken = stack.copy();
                            int shrink = MathsHelper.constrainToRangeInt(count, 0, stack.getCount());
                            stack.shrink(shrink);
                            stackTaken.setCount(shrink);
                            while (!stackTaken.isEmpty()) {
                                cooking.addIngredient(stackTaken);
                            }

                            count -= shrink;
                            if (count <= 0) {
                                break handlers;
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isInInventories(NonNullList<ItemStack> stacks, List<FluidStack> fluids, IngredientStack requiredStack) {
        List<IngredientStack> ingredients = Lists.newArrayList();
        Object2IntMap<Ingredient> map = new Object2IntOpenHashMap<>();
        stacks.forEach(stack -> MapHelper.adjustOrPut(map, Ingredient.registry.getValue(stack), stack.getCount(), stack.getCount()));
        map.forEach((e, k) -> ingredients.add(new IngredientStack(e, k)));
        fluids.forEach((fluid) -> ingredients.add(new IngredientStack(fluid)));
        int found = 0;
        for (IngredientStack ingredientStack: ingredients) {
            if (ingredientStack.matches(requiredStack)) {
                found += ingredientStack.getAmount();
            }

            if (found >= requiredStack.getAmount()) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack getStackFromAppliance(Appliance appliance) {
        switch (appliance) {
            case COUNTER:
                return GastronomyBlocks.COOKWARE.getStackFromEnum(BlockCookware.Cookware.COUNTER);
            case FRYING_PAN:
                return GastronomyBlocks.COOKWARE.getStackFromEnum(BlockCookware.Cookware.FRYING_PAN);
            case MIXER:
                return GastronomyBlocks.COOKWARE.getStackFromEnum(BlockCookware.Cookware.MIXER);
            case OVEN:
                return GastronomyBlocks.COOKWARE.getStackFromEnum(BlockCookware.Cookware.OVEN_OFF);
            case POT:
                return GastronomyBlocks.COOKWARE.getStackFromEnum(BlockCookware.Cookware.POT);
        }

        return ItemStack.EMPTY;
    }
}
