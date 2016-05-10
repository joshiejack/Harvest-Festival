package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;

public class StateHandlerCorn extends StateHandlerDefault {
    public StateHandlerCorn() {
        super(5);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 3) return getState(1);
        else if (stage <= 7) return getState(2);
        else if (stage <= 11) return getState(3);
        else if (stage <= 14) return getState(4);
        else return getState(5);
    }
}