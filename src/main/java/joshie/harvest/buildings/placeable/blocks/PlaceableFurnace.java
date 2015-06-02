package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableFurnace extends PlaceableBlock {
    public PlaceableFurnace(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 3) {
            if (n2) {
                return swap ? 4 : 2;
            } else if (swap) {
                return 5;
            }
        } else if (meta == 5) {
            if (n1) {
                return swap ? 2 : 4;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 4) {
            if (n1) {
                return swap ? 3 : 5;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 2) {
            if (n2) {
                return swap ? 5 : 3;
            } else if (swap) {
                return 4;
            }
        }

        return meta;
    }
}
