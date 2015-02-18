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
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        //TODO:

        if (meta == 1) {
            if (n1) {
                return swap ? 4 : 2;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 2) {
            if (n1) {
                return swap ? 3 : 1;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 3) {
            if (n2) {
                return swap ? 2 : 4;
            } else if (swap) {
                return 1;
            }
        } else if (meta == 4) {
            if (n2) {
                return swap ? 1 : 3;
            } else if (swap) {
                return 2;
            }
        }

        return meta;
    }
}
