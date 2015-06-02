package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableLever extends PlaceableBlock {
    public PlaceableLever(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 4) {
            if (n2) {
                return swap ? 1 : 3;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 3) {
            if (n2) {
                return swap ? 2 : 4;
            } else if (swap) {
                return 1;
            }
        } else if (meta == 2) {
            if (n1) {
                return swap ? 3 : 1;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 1) {
            if (n1) {
                return swap ? 4 : 2;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 12) {
            if (n2) {
                return swap ? 9 : 11;
            } else if (swap) {
                return 10;
            }
        } else if (meta == 11) {
            if (n2) {
                return swap ? 10 : 12;
            } else if (swap) {
                return 9;
            }
        } else if (meta == 10) {
            if (n1) {
                return swap ? 11 : 9;
            } else if (swap) {
                return 12;
            }
        } else if (meta == 9) {
            if (n1) {
                return swap ? 12 : 10;
            } else if (swap) {
                return 11;
            }
        }

        return meta;
    }
}
