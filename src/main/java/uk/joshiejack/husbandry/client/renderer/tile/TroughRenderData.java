package uk.joshiejack.husbandry.client.renderer.tile;

import uk.joshiejack.husbandry.block.BlockTrough;
import uk.joshiejack.husbandry.block.HusbandryBlocks;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TroughRenderData {
    private BlockTrough.Section section;
    private EnumFacing facing;

    public void reset() {
        section = null;
        facing = null;
    }

    public BlockTrough.Section getSection(World world, BlockPos pos) {
        if (section != null) return section;
        else {
            IBlockState state = world.getBlockState(pos);
            IBlockState actualState = state.getActualState(world, pos);
            section = actualState.getValue(HusbandryBlocks.TROUGH.property);
        }

        return section;
    }

    public EnumFacing getFacing(World world, BlockPos pos) {
        if (facing != null) return facing;
        else {
            IBlockState state = world.getBlockState(pos);
            IBlockState actualState = state.getActualState(world, pos);
            facing = actualState.getValue(BlockMultiTileRotatable.FACING);
        }

        return facing;
    }
}
