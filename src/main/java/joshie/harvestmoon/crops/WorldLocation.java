package joshie.harvestmoon.crops;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.core.util.IData;
import net.minecraft.nbt.NBTTagCompound;

public class WorldLocation implements IData {
    protected final static int prime = 31;
    public int dimension;
    public int x;
    public int y;
    public int z;

    public WorldLocation() {}

    public WorldLocation(int dimension, int x, int y, int z) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WorldLocation(WorldLocation location) {
        this.dimension = location.dimension;
        this.x = location.x;
        this.y = location.y;
        this.z = location.z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if ((!(obj instanceof WorldLocation))) return false;
        WorldLocation other = (WorldLocation) obj;
        if (dimension != other.dimension) return false;
        if (x != other.x) return false;
        if (y != other.y) return false;
        if (z != other.z) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = prime * result + dimension;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    /* NBT BASED SAVING */

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("Dimension")) {
            dimension = tag.getByte("Dimension");
        } else dimension = 0;
        x = tag.getInteger("X");
        y = tag.getInteger("Y");
        z = tag.getInteger("Z");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        if (dimension != 0) {
            tag.setByte("Dimension", (byte) dimension);
        }

        tag.setInteger("X", x);
        tag.setInteger("Y", y);
        tag.setInteger("Z", z);
    }

    /* PACKET HANDLING */
    public void toBytes(ByteBuf buf) {
        buf.writeByte(dimension);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public void fromBytes(ByteBuf buf) {
        dimension = buf.readByte();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }
}