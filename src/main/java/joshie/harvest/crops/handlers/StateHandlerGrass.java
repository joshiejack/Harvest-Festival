package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;

public class StateHandlerGrass extends StateHandlerDefault {
    public StateHandlerGrass() {
        super(4);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 3) return getState(1);
        else if (stage <= 6) return getState(2);
        else if (stage <= 8) return getState(3);
        else return getState(4);
    }
}