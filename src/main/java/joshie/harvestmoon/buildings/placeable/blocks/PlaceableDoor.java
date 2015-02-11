package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableDoor extends PlaceableBlock {
    public PlaceableDoor(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 1) {
            if (n2) {
                meta = 3;
                if (swap) {
                    meta = 2;
                }
            } else if (swap) {
                meta = 0;
            }
        } else if (meta == 3) {
            if (n2) {
                meta = 1;
                if (swap) {
                    meta = 0;
                }
            } else if (swap) {
                meta = 2;
            }
        } else if (meta == 0) {
            if (n1) {
                meta = 2;
                if (swap) {
                    meta = 3;
                }
            } else if (swap) {
                meta = 2;
            }
        } else if (meta == 2) {
            if (n1) {
                meta = 0;
                if (swap) {
                    meta = 1;
                }
            } else if (swap) {
                meta = 3;
            }
        } else if (meta == 5) {
            if (n2) {
                meta = 7;
                if (swap) {
                    meta = 6;
                }
            } else if (swap) {
                meta = 4;
            }
        } else if (meta == 7) {
            if (n2) {
                meta = 5;
                if (swap) {
                    meta = 4;
                }
            } else if (swap) {
                meta = 6;
            }
        } else if (meta == 4) {
            if (n1) {
                meta = 6;
                if (swap) {
                    meta = 7;
                }
            } else if (swap) {
                meta = 6;
            }
        } else if (meta == 6) {
            if (n1) {
                meta = 4;
                if (swap) {
                    meta = 5;
                }
            } else if (swap) {
                meta = 7;
            }
        }

        return meta;
    }
}
