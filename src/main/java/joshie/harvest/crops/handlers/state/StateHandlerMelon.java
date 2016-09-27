package joshie.harvest.crops.handlers.state;

import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerMelon extends StateHandlerVanilla {
    public StateHandlerMelon() {
        super(Blocks.MELON_STEM);
    }

    private int getMetaFromStage(int stage) {
        if (stage == 1) return 0;
        else if (stage <= 2) return 1;
        else if (stage <= 3) return 2;
        else if (stage <= 5) return 3;
        else if (stage <= 6) return 4;
        else if (stage <= 8) return 5;
        else if (stage <= 10) return 6;
        else return 7;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        int meta = getMetaFromStage(stage);
        IBlockState state = block.getDefaultState().withProperty(BlockStem.FACING, EnumFacing.UP).withProperty(BlockStem.AGE, meta);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)  {
            if (world.getBlockState(pos.offset(enumfacing)).getBlock() == Blocks.MELON_BLOCK && meta == 7) {
                state = state.withProperty(BlockStem.FACING, enumfacing);
            }
        }

        return state;
    }
}