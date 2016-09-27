package joshie.harvest.crops.handlers.state;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerNetherWart extends StateHandlerVanilla {
    public StateHandlerNetherWart() {
        super(Blocks.NETHER_WART);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        if (stage == 1) return block.getStateFromMeta(0);
        if (stage == 2) return block.getStateFromMeta(1);
        if (stage == 3) return block.getStateFromMeta(2);
        return block.getStateFromMeta(3);
    }
}