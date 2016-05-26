package joshie.harvest.mining.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class WorldLocation {
    public int dimension;
    public BlockPos position;

    public WorldLocation() {
    }

    public WorldLocation(int dimension, BlockPos position) {
        this.dimension = dimension;
        this.position = position;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dimension;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        WorldLocation other = (WorldLocation) obj;
        if (dimension != other.dimension) return false;
        if (position == null) {
            if (other.position != null) return false;
        } else if (!position.equals(other.position)) return false;
        return true;
    }

    /* NBT BASED SAVING */
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("Dimension")) {
            dimension = tag.getByte("Dimension");
        } else dimension = 0;

        int x = tag.getInteger("X");
        int y = tag.getInteger("Y");
        int z = tag.getInteger("Z");
        position = new BlockPos(x, y, z);
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (dimension != 0) {
            tag.setByte("Dimension", (byte) dimension);
        }

        tag.setInteger("X", position.getX());
        tag.setInteger("Y", position.getY());
        tag.setInteger("Z", position.getZ());
    }

    /* PACKET HANDLING */
    public void toBytes(ByteBuf buf) {
        buf.writeByte(dimension);
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
    }

    public void fromBytes(ByteBuf buf) {
        dimension = buf.readByte();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        position = new BlockPos(x, y, z);
    }
}