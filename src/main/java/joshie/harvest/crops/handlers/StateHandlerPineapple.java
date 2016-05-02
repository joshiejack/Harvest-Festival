package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;

public class StateHandlerPineapple extends StateHandlerDefault {
    public StateHandlerPineapple() {
        super(5);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 5) return getState(1);
        else if (stage <= 10) return getState(2);
        else if (stage <= 15) return getState(3);
        else if (stage <= 20) return getState(4);
        else return getState(5);
    }
}