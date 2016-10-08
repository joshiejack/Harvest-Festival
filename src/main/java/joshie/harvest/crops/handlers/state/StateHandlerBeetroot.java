package joshie.harvest.crops.handlers.state;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerBeetroot extends StateHandlerVanilla {
    public StateHandlerBeetroot() {
        super(Blocks.BEETROOTS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        if (stage <= 2) return block.getStateFromMeta(0);
        else if (stage <= 5) return block.getStateFromMeta(1);
        else if (stage <= 7) return block.getStateFromMeta(2);
        else return block.getStateFromMeta(3);
    }
}