package joshie.harvestmoon.network;

import static joshie.harvestmoon.helpers.generic.MCServerHelper.getWorld;
import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.helpers.CropHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketCropRequest implements IMessage, IMessageHandler<PacketCropRequest, IMessage> {
    private int dimension, x, y, z;

    public PacketCropRequest() {}

    public PacketCropRequest(World world, int x, int y, int z) {
        this.dimension = world.provider.dimensionId;
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
        CropHelper.getServerTracker().sendUpdateToClient(ctx.getServerHandler().playerEntity, getWorld(msg.dimension), msg.x, msg.y, msg.z);

        return null;
    }
}