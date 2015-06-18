package joshie.harvest.cooking;

import joshie.harvest.blocks.BlockIcons;
import net.minecraft.util.IIcon;
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
