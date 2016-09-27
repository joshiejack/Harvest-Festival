package joshie.harvest.crops.handlers.state;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerSeedFood extends StateHandlerVanilla {
    public StateHandlerSeedFood(Block block) {
        super(block);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        return block.getStateFromMeta(stage - 1);
    }
}