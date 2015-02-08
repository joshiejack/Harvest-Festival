package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class PlaceableGate extends PlaceableBlock {
    public PlaceableGate(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }
    
    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        return meta;
    }

    @Override
    public void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        super.place(world, x, y, z, n1, n2, swap); //Place a chest, set it's contents afterwards
    }
}
