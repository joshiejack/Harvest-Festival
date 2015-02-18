package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceablePumpkin extends PlaceableBlock {
    public PlaceablePumpkin(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 2) {
            if (n2) {
                return swap ? 3 : 0;
            } else if (swap) {
                return 1;
            }
        } else if (meta == 0) {
            if (n2) {
                return swap ? 1 : 2;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 1) {
            if (n1) {
                return swap ? 0 : 3;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 3) {
            if (n1) {
                return swap ? 2 : 1;
            } else if (swap) {
                return 0;
            }
        }

        return meta;
    }
}
