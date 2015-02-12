package joshie.harvestmoon.mining;

import net.minecraft.nbt.NBTTagCompound;

public class MineLocation extends MineBlockLocation {
    private String name;
    public MineLocation() {}

    public MineLocation(int dimension, int x, int y, int z, int level, String name) {
        super(dimension, x, y, z, level);
        this.name = name;
    }

    public MineLocation(MineLocation location) {
        super(location);
        this.name = location.name;
    }

    public void addLevel() {
        level++;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        name = tag.getString("MineName");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("MineName", name);
    }
}
