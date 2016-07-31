package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public abstract class PenguinPacketLocation extends PenguinPacket {
    protected int dim;
    public BlockPos pos;

    public PenguinPacketLocation() {}

    public PenguinPacketLocation(int dim, BlockPos pos) {
        this.dim = dim;
        this.pos = pos;
    }

    public PenguinPacketLocation(TileEntity tile) {
        this.dim = tile.getWorld().provider.getDimension();
        this.pos = tile.getPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }
}