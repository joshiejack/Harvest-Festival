package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;

public class StateHandlerTomato extends StateHandlerDefault {
    public StateHandlerTomato() {
        super(4);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        System.out.println(getState(1));
        if (stage <= 4) return getState(1);
        else if (stage <= 9) return getState(2);
        else if (stage <= 14) return getState(3);
        else return getState(4);
    }
}