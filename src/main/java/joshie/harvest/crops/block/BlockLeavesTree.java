package joshie.harvest.crops.block;

import joshie.harvest.core.base.block.BlockHFLeaves;
import joshie.harvest.crops.block.BlockFruit.Fruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesTree extends BlockHFLeaves<BlockLeavesTree, Fruit> {
    public BlockLeavesTree() {
        super(Fruit.class);
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }
}
