package joshie.harvestmoon.buildings.placeable.entities;


public abstract class PlaceableHanging extends PlaceableEntity {
    private int facing;

    public PlaceableHanging() {
        super(0, 0, 0);
    }

    public PlaceableHanging(int facing, int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);
        this.facing = facing;
    }

    public int getFacing(boolean n1, boolean n2, boolean swap) {
        if (facing == 0) {
            if (n2) {
                facing = 2;
                if (swap) {
                    facing = 1;
                }
            } else if (swap) {
                facing = 3;
            }
        } else if (facing == 1) {
            if (n1) {
                facing = 3;
                if (swap) {
                    facing = 0;
                }
            } else if (swap) {
                facing = 2;
            }
        } else if (facing == 2) {
            if (n2) {
                facing = 0;
                if (swap) {
                    facing = 3;
                }
            } else if (swap) {
                facing = 1;
            }
        } else if (facing == 3) {
            if (n1) {
                facing = 1;
                if (swap) {
                    facing = 2;
                }
            } else if (swap) {
                facing = 0;
            }
        }

        return facing;
    }

    public int getX(int x, int facing) {
        if (facing == 1) {
            x++;
        } else if (facing == 3) {
            x--;
        }

        return x;
    }

    public int getZ(int z, int facing) {
        if (facing == 0) {
            z--;
        } else if (facing == 2) {
            z++;
        }

        return z;
    }
}
