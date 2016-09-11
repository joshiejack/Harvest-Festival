package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.core.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableDoubleOpposite extends PlaceableDecorative {
    public PlaceableDoubleOpposite(){}
    public PlaceableDoubleOpposite(IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void postPlace(World world, BlockPos pos, Direction direction) {
        world.setBlockState(pos.up(), state.getBlock().getStateFromMeta(9), 2);
    }
}