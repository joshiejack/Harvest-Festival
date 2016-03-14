package joshie.harvest.cooking;

import net.minecraftforge.fluids.Fluid;

public class FluidCookingOil extends Fluid {
    public FluidCookingOil(String name) {
        super(name);
    }

    @Override
    public IIcon getStillIcon() {
        return BlockIcons.OIL_STILL;
    }

    @Override
    public IIcon getFlowingIcon() {
        return BlockIcons.OIL_FLOW;
    }
}
