package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableLadder extends PlaceableBlock {
    public PlaceableLadder(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 1) {
            if (n1) {
                return swap ? 3 : 5;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 3) {
            if (n2) {
                return swap ? 4 : 2;
            } else if (swap) {
                return 5;
            }
        } else if (meta == 4) {
            if (n1) {
                return swap ? 5 : 3;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 5) {
            if (n1) {
                return swap ? 2 : 4;
            } else if (swap) {
                return 3;
            }
        }

        return meta;
    }
}
