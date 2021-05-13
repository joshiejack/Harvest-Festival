package uk.joshiejack.penguinlib.block.interfaces;

import net.minecraftforge.fluids.FluidStack;

public interface ITankProvider {
    void setTank(int slot, FluidStack stack);
}
