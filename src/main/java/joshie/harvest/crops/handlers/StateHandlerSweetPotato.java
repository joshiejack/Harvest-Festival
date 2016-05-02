package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;

public class StateHandlerSweetPotato extends StateHandlerDefault {
    public StateHandlerSweetPotato() {
        super(3);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 3) return getState(1);
        else if (stage <= 5) return getState(2);
        else return getState(3);
    }
}