package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableGate extends PlaceableBlock {
    public PlaceableGate(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 0) {
            if (n2) {
                return swap ? 1 : 2;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 2) {
            if (n2) {
                return swap ? 3 : 0;
            } else if (swap) {
                return 1;
            }
        } else if (meta == 3) {
            if (n1) {
                return swap ? 2 : 1;
            } else if (swap) {
                return 0;
            }
        } else if (meta == 1) {
            if (n1) {
                return swap ? 0 : 3;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 4) {
            if (n2) {
                return swap ? 5 : 6;
            } else if (swap) {
                return 7;
            }
        } else if (meta == 6) {
            if (n2) {
                return swap ? 7 : 4;
            } else if (swap) {
                return 5;
            }
        } else if (meta == 7) {
            if (n1) {
                return swap ? 6 : 5;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 5) {
            if (n1) {
                return swap ? 4 : 7;
            } else if (swap) {
                return 6;
            }
        }

        return meta;
    }
}
