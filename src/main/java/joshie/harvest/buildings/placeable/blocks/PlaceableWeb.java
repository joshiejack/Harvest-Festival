package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableWeb extends PlaceableBlock {
    @Override
    public boolean prePlace (World world, BlockPos pos, Direction direction) {
        return world.rand.nextInt() == 3 && super.prePlace(world, pos, direction);
    }
}