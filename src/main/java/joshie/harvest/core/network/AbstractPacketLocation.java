package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class AbstractPacketLocation implements IMessage {
    protected int dim;
    public BlockPos pos;
    
    public AbstractPacketLocation() {}

    public AbstractPacketLocation(int dim, BlockPos pos) {
        this.dim = dim;
        this.pos = pos;
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