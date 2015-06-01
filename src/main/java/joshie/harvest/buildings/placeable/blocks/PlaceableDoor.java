package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.buildings.placeable.Placeable.PlacementStage;
import net.minecraft.block.Block;

public class PlaceableDoor extends PlaceableBlock {
    public PlaceableDoor(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }
    
    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 1) {
            if (n2) {
                return swap ? 2 : 3;
            } else if (swap) {
                return 0;
            }
        } else if (meta == 3) {
            if (n2) {
                return swap ? 0 : 1;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 0) {
            if (n1) {
                return swap ? 3 : 2;
            } else if (swap) {
                return 1;
            }
        } else if (meta == 2) {
            if (n1) {
                return swap ? 1 : 0;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 5) {
            if (n2) {
                return swap ? 6 : 7;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 7) {
            if (n2) {
                return swap ? 4 : 5;
            } else if (swap) {
                return 6;
            }
        } else if (meta == 4) {
            if (n1) {
                return swap ? 7 : 6;
            } else if (swap) {
                return 5;
            }
        } else if (meta == 6) {
            if (n1) {
                return swap ? 5 : 4;
            } else if (swap) {
                return 7;
            }
        }

        return meta;
    }
}
