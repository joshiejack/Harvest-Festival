package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableSignFloor extends PlaceableSignWall {
    private String[] text;

    public PlaceableSignFloor(Block block, int meta, int offsetX, int offsetY, int offsetZ, String[] text) {
        super(block, meta, offsetX, offsetY, offsetZ, text);
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 8) {
            if (n2) {
                meta = 0;
                if (swap) {
                    meta = 12;
                }
            } else if (swap) {
                meta = 4;
            }
        } else if (meta == 0) {
            if (n2) {
                meta = 8;
                if (swap) {
                    meta = 4;
                }
            } else if (swap) {
                meta = 12;
            }
        } else if (meta == 4) {
            if (n1) {
                meta = 12;
                if (swap) {
                    meta = 0;
                }
            } else if (swap) {
                meta = 8;
            }
        } else if (meta == 12) {
            if (n1) {
                meta = 4;
                if (swap) {
                    meta = 8;
                }
            } else if (swap) {
                meta = 0;
            }
        }
        
        return meta;
    }
}
