package joshie.harvest.api.crops;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerBasic extends StateHandlerDefault<Crop> {
    private final int[] values;

    public StateHandlerBasic(int... values) {
        super(values.length);
        this.values = values;
    }

    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, Crop crop, int stage, boolean withered) {
        for (int i = 0; i < values.length; i++) {
            if (stage <= values[i]) return getState(i + 1);
        }

        return getState(values.length);
    }
}
