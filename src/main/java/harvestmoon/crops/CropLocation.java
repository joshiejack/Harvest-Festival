package harvestmoon.crops;

import harvestmoon.util.IData;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class CropLocation implements IData {
    private final int prime = 31;
    public int dimension;
    public int x;
    public int y;
    public int z;
    
    public CropLocation() {}
    public CropLocation(int dimension, int x, int y, int z) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CropLocation(CropLocation location) {
        this.dimension = location.dimension;
        this.x = location.x;
        this.y = location.y;
        this.z = location.z;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CropLocation other = (CropLocation) obj;
        if (dimension != other.dimension)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
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
    public void writeToNBT(NBTTagCompound tag) {
        tag.setByte("Dimension", (byte) dimension);
        tag.setInteger("X", x);
        tag.setInteger("Y", y);
        tag.setInteger("Z", z);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        dimension = tag.getByte("Dimension");
        x = tag.getInteger("X");
        y = tag.getInteger("Y");
        z = tag.getInteger("Z");
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