package uk.joshiejack.gastronomy.inventory.slot;

import uk.joshiejack.gastronomy.cooking.Ingredient;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.penguinlib.inventory.slot.SlotUnlimited;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class SlotFood extends SlotUnlimited {
    public SlotFood(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    public static boolean isValid(ItemStack stack) {
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        return fluid != null && Ingredient.ingredients.get(fluid.getFluid().getName()) != null ||
                stack.getItem() instanceof ItemFood || stack.getItem() == Items.CAKE ||
                !Ingredient.registry.getValue(stack).isNone() ||
                Recipe.RECIPE_BY_STACK.values().stream().anyMatch((r) -> r.getResult().isItemEqual(stack));
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return isValid(stack);
    }
}
