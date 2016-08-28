package joshie.harvest.crops.handlers.state;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class StateHandlerStem extends StateHandlerVanilla {
    public StateHandlerStem(Block block) {
        super(block);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage == 0)  return block.getStateFromMeta(0);
        if (stage == 1)  return block.getStateFromMeta(1);
        else if (stage <= 2) return block.getStateFromMeta(2);
        else if (stage <= 4) return block.getStateFromMeta(3);
        else if (stage <= 8) return block.getStateFromMeta(4);
        else if (stage <= 12) return block.getStateFromMeta(5);
        else if (stage <= 14) return block.getStateFromMeta(6);
        else return block.getStateFromMeta(7);
    }
}