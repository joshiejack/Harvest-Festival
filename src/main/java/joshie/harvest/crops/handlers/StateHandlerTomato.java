package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;

public class StateHandlerTomato extends StateHandlerDefault {
    public StateHandlerTomato() {
        super(5);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 2) return getState(1);
        else if (stage <= 4) return getState(2);
        else if (stage <= 6) return getState(3);
        else if (stage <= 9) return getState(4);
        else return getState(5);
    }
}