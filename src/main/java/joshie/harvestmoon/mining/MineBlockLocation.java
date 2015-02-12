package joshie.harvestmoon.mining;

import joshie.harvestmoon.crops.WorldLocation;
import net.minecraft.nbt.NBTTagCompound;

public class MineBlockLocation extends WorldLocation {
    protected int level;
    public MineBlockLocation() {}

    public MineBlockLocation(int dimension, int x, int y, int z, int level) {
        super(dimension, x, y, z);
        this.level = level;
    }

    public MineBlockLocation(MineBlockLocation location) {
        super(location);
        this.level = location.level;
    }

    public int getLevel() {
        return level;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        level = tag.getInteger("Level");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("Level", level);
    }
}
