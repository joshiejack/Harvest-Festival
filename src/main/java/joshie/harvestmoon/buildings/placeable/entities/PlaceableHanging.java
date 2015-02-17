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
                return swap ? 1 : 2;
            } else if (swap) {
                return 3;
            }
        } else if (facing == 1) {
            if (n1) {
                return swap ? 0 : 3;
            } else if (swap) {
                return 2;
            }
        } else if (facing == 2) {
            if (n2) {
                return swap ? 3 : 0;
            } else if (swap) {
                return 1;
            }
        } else if (facing == 3) {
            if (n1) {
                return swap ? 2 : 1;
            } else if (swap) {
                return 0;
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
