package joshie.harvest.buildings.placeable.entities;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.util.EnumFacing.*;

public abstract class PlaceableHanging extends PlaceableEntity {
    private EnumFacing facing;

    public PlaceableHanging() {
        super(BlockPos.ORIGIN);
    }

    public PlaceableHanging(EnumFacing facing, BlockPos offsetPos) {
        super(offsetPos);
        this.facing = facing;
    }

    public EnumFacing getFacing(boolean n1, boolean n2, boolean swap) {
        if (facing == DOWN) {
            if (n2) {
                return swap ? UP : NORTH;
            } else if (swap) {
                return SOUTH;
            }
        } else if (facing == UP) {
            if (n1) {
                return swap ? DOWN : SOUTH;
            } else if (swap) {
                return NORTH;
            }
        } else if (facing == NORTH) {
            if (n2) {
                return swap ? SOUTH : DOWN;
            } else if (swap) {
                return UP;
            }
        } else if (facing == SOUTH) {
            if (n1) {
                return swap ? NORTH : UP;
            } else if (swap) {
                return DOWN;
            }
        }

        return facing;
    }

    public int getX(int x, EnumFacing facing) {
        if (facing == UP) {
            x++;
        } else if (facing == SOUTH) {
            x--;
        }

        return x;
    }

    public int getZ(int z, EnumFacing facing) {
        if (facing == DOWN) {
            z--;
        } else if (facing == NORTH) {
            z++;
        }
        return z;
    }
}