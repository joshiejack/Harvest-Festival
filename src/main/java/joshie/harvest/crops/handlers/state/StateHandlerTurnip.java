package joshie.harvest.crops.handlers.state;

import net.minecraft.block.state.IBlockState;

public class StateHandlerTurnip extends StateHandlerDefault {
    public StateHandlerTurnip() {
        super(3);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 2) return getState(1);
        else if (stage <= 4) return getState(2);
        else return getState(3);
    }
}