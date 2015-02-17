package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableButton extends PlaceableBlock {
    public PlaceableButton(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        //TODO:

        if (meta == 1) {
            if (n1) {
                meta = 2;
                if (swap) {
                    meta = 4;
                }
            } else if (swap) {
                meta = 3;
            }
        } else if (meta == 2) {
            if (n1) {
                meta = 1;
                if (swap) {
                    meta = 3;
                }
            } else if (swap) {
                meta = 4;
            }
        } else if (meta == 3) {
            if (n2) {
                meta = 4;
                if (swap) {
                    meta = 2;
                }
            } else if (swap) {
                meta = 1;
            }
        } else if (meta == 4) {
            if (n2) {
                meta = 3;
                if (swap) {
                    meta = 1;
                }
            } else if (swap) {
                meta = 2;
            }
        }

        return meta;
    }
}
