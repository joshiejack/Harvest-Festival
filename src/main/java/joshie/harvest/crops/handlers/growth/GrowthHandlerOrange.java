package joshie.harvest.crops.handlers.growth;

import joshie.harvest.api.trees.GrowthHandlerTree;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrowthHandlerOrange extends GrowthHandlerTree {
    @Override
    protected void growTree(World world, BlockPos pos) {
        world.setBlockState(pos.up(), Blocks.LOG.getDefaultState());
    }

    @Override
    protected void growFruit(World world, BlockPos pos) {
        //Make sure to check how much fruit is on the tree before growing any more
        world.setBlockState(pos.east(), Blocks.FURNACE.getDefaultState());
    }
}