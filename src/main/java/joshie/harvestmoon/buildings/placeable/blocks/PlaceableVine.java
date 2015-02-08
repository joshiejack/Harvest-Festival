package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableVine extends PlaceableBlock {
    public PlaceableVine(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 1) {
            if (n2) {
                meta = 4;
                if (swap) {
                    meta = 2;
                }
            } else if (swap) {
                meta = 8;
            }
        } else if (meta == 4) {
            if (n2) {
                meta = 1;
                if (swap) {
                    meta = 8;
                }
            } else if (swap) {
                meta = 2;
            }
        } else if (meta == 8) {
            if (n1) {
                meta = 2;
                if (swap) {
                    meta = 4;
                }
            } else if (swap) {
                meta = 1;
            }
        } else if (meta == 2) {
            if (n1) {
                meta = 8;
                if (swap) {
                    meta = 1;
                }
            } else if (swap) {
                meta = 4;
            }
        }

        return meta;
    }
}
