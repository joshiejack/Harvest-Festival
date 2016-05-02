package joshie.harvest.crops.handlers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class StateHandlerWheat extends StateHandlerVanilla {
    public StateHandlerWheat() {
        super(Blocks.WHEAT);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage <= 2)  return block.getStateFromMeta(0);
        if (stage <= 5)  return block.getStateFromMeta(1);
        else if (stage <= 9) return block.getStateFromMeta(2);
        else if (stage <= 12) return block.getStateFromMeta(3);
        else if (stage <= 15) return block.getStateFromMeta(4);
        else if (stage <= 20) return block.getStateFromMeta(5);
        else if (stage <= 27) return block.getStateFromMeta(6);
        else return block.getStateFromMeta(7);
    }
}