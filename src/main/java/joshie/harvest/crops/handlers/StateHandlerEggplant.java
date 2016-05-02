package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;

public class StateHandlerEggplant extends StateHandlerDefault {
    public StateHandlerEggplant() {
        super(4);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 3) return getState(1);
        else if (stage <= 6) return getState(2);
        else if (stage <= 9) return getState(3);
        else return getState(4);
    }
}