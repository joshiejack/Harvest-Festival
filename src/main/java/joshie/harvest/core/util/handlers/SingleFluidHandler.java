package joshie.harvest.core.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class SingleFluidHandler extends FluidHandlerItemStack {
    private final Fluid fluid;

    public SingleFluidHandler(ItemStack container, Fluid fluid, int capacity) {
        super(container, capacity);
        this.fluid = fluid;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.getFluid().equals(this.fluid);
    }
}
