package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableAnvil extends PlaceableBlock {
    public PlaceableAnvil(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }
    
    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 3) {
            if (n2) {
                meta = 1;
                if (swap) {
                    meta = 0;
                }
            } else if (swap) {
                meta = 2;
            }
        } else if (meta == 1) {
            if (n2) {
                meta = 3;
                if (swap) {
                    meta = 2;
                }
            } else if (swap) {
                meta = 0;
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
        } else if (meta == 0) {
            if (n1) {
                meta = 2;
                if (swap) {
                    meta = 3;
                }
            } else if (swap) {
                meta = 1;
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
        } else if (meta == 5) {
            if (n2) {
                meta = 7;
                if (swap) {
                    meta = 6;
                }
            } else if (swap) {
                meta = 4;
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
        } else if (meta == 4) {
            if (n1) {
                meta = 6;
                if (swap) {
                    meta = 7;
                }
            } else if (swap) {
                meta = 5;
            }
        } else if (meta == 11) {
            if (n2) {
                meta = 9;
                if (swap) {
                    meta = 8;
                }
            } else if (swap) {
                meta = 10;
            }
        } else if (meta == 9) {
            if (n2) {
                meta = 11;
                if (swap) {
                    meta = 10;
                }
            } else if (swap) {
                meta = 8;
            }
        } else if (meta == 10) {
            if (n1) {
                meta = 8;
                if (swap) {
                    meta = 9;
                }
            } else if (swap) {
                meta = 11;
            }
        } else if (meta == 8) {
            if (n1) {
                meta = 10;
                if (swap) {
                    meta = 11;
                }
            } else if (swap) {
                meta = 9;
            }
        }

        return meta;
    }
}
