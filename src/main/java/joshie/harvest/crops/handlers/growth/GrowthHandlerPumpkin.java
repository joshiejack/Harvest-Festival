package joshie.harvest.crops.handlers.growth;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class GrowthHandlerPumpkin extends GrowthHandlerSide {
    public GrowthHandlerPumpkin() {
        super(Blocks.PUMPKIN);
    }

    @Override
    protected IBlockState getBlockState(World world) {
        return block.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.HORIZONTALS[world.rand.nextInt(EnumFacing.HORIZONTALS.length)]);
    }
}
