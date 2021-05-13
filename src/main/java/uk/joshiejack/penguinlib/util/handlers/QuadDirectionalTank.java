package uk.joshiejack.penguinlib.util.handlers;

import com.google.common.collect.Lists;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class QuadDirectionalTank extends MultiFluidHandler {
    public QuadDirectionalTank() {
        super(4, Fluid.BUCKET_VOLUME);
    }

    public boolean hasTank(@Nullable EnumFacing facing) {
        return facing != null && facing.getHorizontalIndex() >= 0;
    }

    public IFluidHandler getHandlerFromFacing(EnumFacing facing) {
        return tanks[facing.getHorizontalIndex()];
    }

    public FluidStack getFluid(int i) {
        return tanks[i].getFluid();
    }

    public void set(int slot, FluidStack stack) {
        tanks[slot].setFluid(stack);
    }

    public List<FluidStack> getFluids() {
        List<FluidStack> list = Lists.newArrayList();
        for (FluidTank tank: tanks) {
            if (tank.getFluid() != null) list.add(tank.getFluid());
        }

        return list;
    }

    public IFluidHandler get(int i) {
        return tanks[i];
    }
}
