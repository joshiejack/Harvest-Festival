package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;

public class StateHandlerCorn extends StateHandlerDefault {
    public StateHandlerCorn() {
        super(5);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 4) return getState(1);
        else if (stage <= 9) return getState(2);
        else if (stage <= 14) return getState(3);
        else return getState(4);
    }
}