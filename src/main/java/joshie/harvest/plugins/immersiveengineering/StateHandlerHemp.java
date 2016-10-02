package joshie.harvest.plugins.immersiveengineering;

import joshie.harvest.crops.handlers.state.StateHandlerVanilla;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerHemp extends StateHandlerVanilla {
    public StateHandlerHemp(Block block) {
        super(block);
    }

    static int getMetaFromStage(int stage) {
        if (stage <= 3) return 0;
        else if (stage <= 6) return 1;
        else if (stage <= 10) return 2;
        else if (stage <= 14) return 3;
        else return 4;
    }

    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        if (section == PlantSection.TOP) return block.getStateFromMeta(5);
        return block.getStateFromMeta(getMetaFromStage(stage));
    }
}
