package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceablePumpkin extends PlaceableBlock {
    public PlaceablePumpkin(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 2) {
            if (n2) {
                meta = 0;
                if (swap) {
                    meta = 3;
                }
            } else if (swap) {
                meta = 1;
            }
        } else if (meta == 0) {
            if (n2) {
                meta = 2;
                if (swap) {
                    meta = 1;
                }
            } else if (swap) {
                meta = 3;
            }
        } else if (meta == 1) {
            if (n1) {
                meta = 3;
                if (swap) {
                    meta = 0;
                }
            } else if (swap) {
                meta = 2;
            }
        } else if (meta == 3) {
            if (n1) {
                meta = 1;
                if (swap) {
                    meta = 2;
                }
            } else if (swap) {
                meta = 0;
            }
        }

        return meta;
    }
}
