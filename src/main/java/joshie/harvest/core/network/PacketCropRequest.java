package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static joshie.harvest.core.helpers.generic.MCServerHelper.getWorld;

public class PacketCropRequest implements IMessage, IMessageHandler<PacketCropRequest, IMessage> {
    private int dimension, x, y, z;

    public PacketCropRequest() {}

    public PacketCropRequest(World world, int x, int y, int z) {
        this.dimension = world.provider.getDimensionId();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(dimension);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readByte();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public IMessage onMessage(PacketCropRequest msg, MessageContext ctx) {     
        HFTrackers.getCropTracker().sendUpdateToClient(ctx.getServerHandler().playerEntity, getWorld(msg.dimension), msg.x, msg.y, msg.z);

        return null;
    }
}