package harvestmoon.buildings;

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

    public Building setName(String name) {
        this.name = name;
        return this;
    }
    
    public Building init() {
        return this;
    }

    public abstract boolean generate(World world, int x, int y, int z);

    @Override
    public boolean equals(Object o) {
        return name.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
