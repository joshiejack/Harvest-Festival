package joshie.harvestmoon.buildings.placeable.blocks;

import net.minecraft.block.Block;

public class PlaceableTrapDoor extends PlaceableBlock {
    public PlaceableTrapDoor(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 0) {
            if (n2) {
                meta = 1;
                if (swap) {
                    meta = 3;
                }
            } else if (swap) {
                meta = 2;
            }
        } else if (meta == 1) {
            if (n2) {
                meta = 0;
                if (swap) {
                    meta = 2;
                }
            } else if (swap) {
                meta = 3;
            }
        } else if (meta == 2) {
            if (n1) {
                meta = 3;
                if (swap) {
                    meta = 1;
                }
            } else if (swap) {
                meta = 0;
            }
        } else if (meta == 3) {
            if (n1) {
                meta = 2;
                if (swap) {
                    meta = 0;
                }
            } else if (swap) {
                meta = 1;
            }
        } else if (meta == 8) {
            if (n2) {
                meta = 9;
                if (swap) {
                    meta = 11;
                }
            } else if (swap) {
                meta = 10;
            }
        } else if (meta == 9) {
            if (n2) {
                meta = 8;
                if (swap) {
                    meta = 10;
                }
            } else if (swap) {
                meta = 11;
            }
        } else if (meta == 10) {
            if (n1) {
                meta = 11;
                if (swap) {
                    meta = 9;
                }
            } else if (swap) {
                meta = 8;
            }
        } else if (meta == 11) {
            if (n1) {
                meta = 10;
                if (swap) {
                    meta = 8;
                }
            } else if (swap) {
                meta = 9;
            }
        } else if (meta == 12) {
            if (n2) {
                meta = 13;
                if (swap) {
                    meta = 15;
                }
            } else if (swap) {
                meta = 14;
            }
        } else if (meta == 13) {
            if (n2) {
                meta = 12;
                if (swap) {
                    meta = 14;
                }
            } else if (swap) {
                meta = 15;
            }
        } else if (meta == 14) {
            if (n1) {
                meta = 15;
                if (swap) {
                    meta = 13;
                }
            } else if (swap) {
                meta = 12;
            }
        } else if (meta == 15) {
            if (n1) {
                meta = 14;
                if (swap) {
                    meta = 12;
                }
            } else if (swap) {
                meta = 13;
            }
        } else if (meta == 4) {
            if (n2) {
                meta = 5;
                if (swap) {
                    meta = 7;
                }
            } else if (swap) {
                meta = 6;
            }
        } else if (meta == 5) {
            if (n2) {
                meta = 4;
                if (swap) {
                    meta = 6;
                }
            } else if (swap) {
                meta = 7;
            }
        } else if (meta == 6) {
            if (n1) {
                meta = 7;
                if (swap) {
                    meta = 5;
                }
            } else if (swap) {
                meta = 4;
            }
        } else if (meta == 7) {
            if (n1) {
                meta = 6;
                if (swap) {
                    meta = 4;
                }
            } else if (swap) {
                meta = 5;
            }
        }

        return meta;
    }
}
