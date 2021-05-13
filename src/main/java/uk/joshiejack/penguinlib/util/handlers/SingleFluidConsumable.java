package uk.joshiejack.penguinlib.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class SingleFluidConsumable extends FluidHandlerItemStack {
    private final FluidStack stack;

    public SingleFluidConsumable(ItemStack container, Fluid fluid) {
        super(container, 1000);
        this.stack = new FluidStack(fluid, 1000);
    }

    @Override
    public FluidStack getFluid() {
        return stack;
    }

    @Override
    protected void setContainerToEmpty() {
        container.shrink(1);
    }
}