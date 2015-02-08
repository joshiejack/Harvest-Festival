package joshie.harvestmoon.buildings.placeable;

import net.minecraft.world.World;

public abstract class Placeable {
    protected int offsetX;
    protected int offsetY;
    protected int offsetZ;

    public Placeable(int offsetX, int offsetY, int offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.BLOCKS;
    }

    public void place(World world, int xCoord, int yCoord, int zCoord, boolean n1, boolean n2, boolean swap, PlacementStage stage) {
        if (canPlace(stage)) {
            int y = offsetY;
            int x = n1 ? -offsetX : offsetX;
            int z = n2 ? -offsetZ : offsetZ;
            if (swap) {
                int xClone = x; //Create a copy of X
                x = z; //Set x to z
                z = xClone; //Set z to the old value of x
            }

            place(world, xCoord + x, yCoord + y, zCoord + z, n1, n2, swap);
        }
    }

    public abstract void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap);

    public static enum PlacementStage {
        BLOCKS, TORCHES, ENTITIES;
    }
}
