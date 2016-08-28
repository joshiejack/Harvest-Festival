package joshie.harvest.crops.handlers.state;

import net.minecraft.block.state.IBlockState;

public class StateHandlerOnion extends StateHandlerDefault {
    public StateHandlerOnion() {
        super(3);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 3) return getState(1);
        else if (stage <= 7) return getState(2);
        else return getState(3);
    }
}