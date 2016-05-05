package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableWeb extends PlaceableBlock {
    public PlaceableWeb(Block web, int meta, int x, int y, int z) {
        super(web, meta, x, y, z);
    }

    @Override
    public boolean prePlace (UUID owner, World world, BlockPos pos, Direction direction) {
        return world.rand.nextInt() == 3 && super.prePlace(owner, world, pos, direction);
    }
}