package harvestmoon.network;

import static harvestmoon.HarvestMoon.handler;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
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
        handler.getServer().getCropTracker().sendUpdateToClient(ctx.getServerHandler().playerEntity, DimensionManager.getWorld(msg.dimension), msg.x, msg.y, msg.z);

        return null;
    }
}