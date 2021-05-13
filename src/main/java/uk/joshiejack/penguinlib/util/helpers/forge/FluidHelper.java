package uk.joshiejack.penguinlib.util.helpers.forge;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidHelper {
    public static int getFluidCapacityFromStack(ItemStack stack) {
        IFluidTankProperties properties = CapabilityHelper.getCapabilityFromStack(stack, CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).getTankProperties()[0];
        if (properties.getContents() == null) {
            return 0;
        } else return properties.getContents().amount;
    }

    public static boolean fillContainer(ItemStack stack, int maxWater) {
        return CapabilityHelper.getCapabilityFromStack(stack, CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).fill(new FluidStack(FluidRegistry.WATER, maxWater), true) > 0;
    }

    public static void drainContainer(ItemStack stack, int amount) {
        CapabilityHelper.getCapabilityFromStack(stack, CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).drain(new FluidStack(FluidRegistry.WATER, amount), true);
    }
}
