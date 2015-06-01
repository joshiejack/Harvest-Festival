package joshie.harvest.animals;

import joshie.harvest.api.WorldLocation;
import net.minecraft.nbt.NBTTagCompound;

public class ValueLocation extends WorldLocation {
    private int value;

    public ValueLocation() {}
    public ValueLocation(int dimensionId, int x, int y, int z) {
        super(dimensionId, x, y, z);
    }

    public int getValue() {
        return value;
    }

    public void decr() {
        value--;
    }

    public void incr() {
        value++;
    }

    public void reset() {
        value = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        value = nbt.getInteger("Value");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Value", value);
    }
}
