package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceablePillar extends PlaceableBlock {
    public PlaceablePillar(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (swap) {
            if (meta == 3 || meta == 4) {
                return meta == 3 ? 4 : 3;
            }
        }

        return meta;
    }
}
