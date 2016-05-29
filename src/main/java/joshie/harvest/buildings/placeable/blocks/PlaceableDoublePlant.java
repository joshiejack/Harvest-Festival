package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableDoublePlant extends PlaceableDecorative {
    public PlaceableDoublePlant(){}
    public PlaceableDoublePlant(IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
    }

    @Override
    public void postPlace(World world, BlockPos pos, Direction direction) {
        world.setBlockState(pos.up(), state.getBlock().getStateFromMeta(8), 2);
    }
}