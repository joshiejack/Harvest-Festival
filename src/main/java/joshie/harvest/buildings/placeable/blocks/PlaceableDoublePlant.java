package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableDoublePlant extends PlaceableBlock {
    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Direction direction) {
        world.setBlockState(pos.up(), state.getBlock().getStateFromMeta(8), 2);
    }
}