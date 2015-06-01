package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableAnvil extends PlaceableBlock {
    public PlaceableAnvil(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 3) {
            if (n2) {
                return swap ? 0 : 1;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 1) {
            if (n2) {
                return swap ? 2 : 3;
            } else if (swap) {
                return 0;
            }
        } else if (meta == 2) {
            if (n1) {
                return swap ? 1 : 0;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 0) {
            if (n1) {
                return swap ? 3 : 2;
            } else if (swap) {
                return 1;
            }
        } else if (meta == 7) {
            if (n2) {
                return swap ? 4 : 5;
            } else if (swap) {
                return 6;
            }
        } else if (meta == 5) {
            if (n2) {
                return swap ? 6 : 7;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 6) {
            if (n1) {
                return swap ? 5 : 4;
            } else if (swap) {
                return 7;
            }
        } else if (meta == 4) {
            if (n1) {
                return swap ? 7 : 6;
            } else if (swap) {
                return 5;
            }
        } else if (meta == 11) {
            if (n2) {
                return swap ? 8 : 9;
            } else if (swap) {
                return 10;
            }
        } else if (meta == 9) {
            if (n2) {
                return swap ? 10 : 11;
            } else if (swap) {
                return 8;
            }
        } else if (meta == 10) {
            if (n1) {
                return swap ? 9 : 8;
            } else if (swap) {
                return 11;
            }
        } else if (meta == 8) {
            if (n1) {
                return swap ? 11 : 10;
            } else if (swap) {
                return 9;
            }
        }

        return meta;
    }
}
