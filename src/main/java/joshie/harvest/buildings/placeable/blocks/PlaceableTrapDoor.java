package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableTrapDoor extends PlaceableBlock {
    public PlaceableTrapDoor(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 0) {
            if (n2) {
                return swap ? 3 : 1;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 1) {
            if (n2) {
                return swap ? 2 : 0;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 2) {
            if (n1) {
                return swap ? 1 : 3;
            } else if (swap) {
                return 0;
            }
        } else if (meta == 3) {
            if (n1) {
                return swap ? 0 : 2;
            } else if (swap) {
                return 1;
            }
        } else if (meta == 8) {
            if (n2) {
                return swap ? 11 : 9;
            } else if (swap) {
                return 10;
            }
        } else if (meta == 9) {
            if (n2) {
                return swap ? 10 : 8;
            } else if (swap) {
                return 11;
            }
        } else if (meta == 10) {
            if (n1) {
                return swap ? 9 : 11;
            } else if (swap) {
                return 8;
            }
        } else if (meta == 11) {
            if (n1) {
                return swap ? 8 : 10;
            } else if (swap) {
                return 9;
            }
        } else if (meta == 12) {
            if (n2) {
                return swap ? 15 : 13;
            } else if (swap) {
                return 14;
            }
        } else if (meta == 13) {
            if (n2) {
                return swap ? 14 : 12;
            } else if (swap) {
                return 15;
            }
        } else if (meta == 14) {
            if (n1) {
                return swap ? 13 : 15;
            } else if (swap) {
                return 12;
            }
        } else if (meta == 15) {
            if (n1) {
                return swap ? 12 : 14;
            } else if (swap) {
                return 13;
            }
        } else if (meta == 4) {
            if (n2) {
                return swap ? 7 : 5;
            } else if (swap) {
                return 6;
            }
        } else if (meta == 5) {
            if (n2) {
                return swap ? 6 : 4;
            } else if (swap) {
                return 7;
            }
        } else if (meta == 6) {
            if (n1) {
                return swap ? 5 : 7;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 7) {
            if (n1) {
                return swap ? 4 : 6;
            } else if (swap) {
                return 5;
            }
        }

        return meta;
    }
}
