package joshie.harvestmoon.buildings;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class Building {
    public static final ArrayList<Building> buildings = new ArrayList();
    
    //Data for all the blocks
    protected Block[] blocks;
    protected int[] metas;
    protected int[] offsetX;
    protected int[] offsetY;
    protected int[] offsetZ;
    private String name;
    
    public String getName() {
        return name;
    }

    public Building setName(String name) {
        this.name = name;
        return this;
    }
    
    public Building init() {
        return this;
    }

    public boolean generate(World world, int xCoord, int yCoord, int zCoord) {
        if (!world.isRemote) {
            boolean n1 = world.rand.nextBoolean();
            boolean n2 = world.rand.nextBoolean();
            boolean swap = world.rand.nextBoolean();
            //foundation(world, x, y, z, xWidth, zWidth);
            for (int i = 0; i < offsetX.length; i++) {
                int y = offsetY[i];
                int x = n1 ? -offsetX[i] : offsetX[i];
                int z = n2 ? -offsetZ[i] : offsetZ[i];
                if (swap) {
                    int xClone = x; //Create a copy of X
                    x = z; //Set x to z
                    z = xClone; //Set z to the old value of x
                }

                Block block = blocks[i];
                int meta = metas[i];
                if (meta == 0) {
                    world.setBlock(xCoord + x, yCoord + y, zCoord + z, block);
                } else {
                    world.setBlock(xCoord + x, yCoord + y, zCoord + z, block, meta, 2);
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        return name.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
