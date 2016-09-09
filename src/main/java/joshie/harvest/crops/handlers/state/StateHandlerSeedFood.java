package joshie.harvest.crops.handlers.state;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class StateHandlerSeedFood extends StateHandlerVanilla {
    public StateHandlerSeedFood(Block block) {
        super(block);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        return block.getStateFromMeta(stage - 1);
    }
}