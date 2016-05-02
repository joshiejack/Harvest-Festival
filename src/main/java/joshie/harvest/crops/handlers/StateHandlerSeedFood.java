package joshie.harvest.crops.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class StateHandlerSeedFood extends StateHandlerVanilla {
    public StateHandlerSeedFood(Block block) {
        super(block);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage == 1) return block.getStateFromMeta(0);
        if (stage == 2 || stage == 3 || stage == 4) return block.getStateFromMeta(1);
        if (stage == 5 || stage == 6 || stage == 7) return block.getStateFromMeta(2);
        return block.getStateFromMeta(3);
    }
}