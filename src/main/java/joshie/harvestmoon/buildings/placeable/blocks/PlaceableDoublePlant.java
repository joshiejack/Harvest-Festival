package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class PlaceableDoublePlant extends PlaceableBlock {
    public PlaceableDoublePlant(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        super.place(world, x, y, z, n1, n2, swap);
        world.setBlock(x, y + 1, z, block, 8, 2);
    }
}
