package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.penguin.PenguinPacket;
import net.minecraft.client.network.NetHandlerPlayClient;

public abstract class AbstractPacketDimension extends PenguinPacket {
    protected int dimension;
    public AbstractPacketDimension() {}
    public AbstractPacketDimension(int dim) {
        this.dimension = dim;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
    }

    @Override
    public void handleQueuedClient(NetHandlerPlayClient handler) {
        if (MCClientHelper.getWorld().provider.getDimension() == dimension) {
            handlePacket(MCClientHelper.getPlayer());
        }
    }
}