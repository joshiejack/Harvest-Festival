package joshie.harvest.cooking;

import joshie.harvest.blocks.BlockIcons;
import net.minecraftforge.fluids.Fluid;

public class FluidCookingMilk extends Fluid {
    public FluidCookingMilk(String name) {
        super(name);
    }

    @Override
    public IIcon getStillIcon() {
        return BlockIcons.MILK_STILL;
    }

    @Override
    public IIcon getFlowingIcon() {
        return BlockIcons.MILK_FLOW;
    }
}
