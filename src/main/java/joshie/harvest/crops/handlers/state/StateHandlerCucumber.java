package joshie.harvest.crops.handlers.state;

import joshie.harvest.api.crops.StateHandlerDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerCucumber extends StateHandlerDefault {
    public StateHandlerCucumber() {
        super(4);
    }

    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        if (stage <= 4) return getState(1);
        else if (stage <= 7) return getState(2);
        else if (stage <= 9) return getState(3);
        else return getState(4);
    }
}