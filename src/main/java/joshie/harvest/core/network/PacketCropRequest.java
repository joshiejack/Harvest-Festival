package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static joshie.harvest.core.helpers.generic.MCServerHelper.getWorld;

public class PacketCropRequest implements IMessage, IMessageHandler<PacketCropRequest, IMessage> {
    private int dimension;
    private BlockPos pos;

    public PacketCropRequest() {
    }

    public PacketCropRequest(World world, BlockPos pos) {
        this.dimension = world.provider.getDimension();
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(dimension);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readByte();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        pos = new BlockPos(x, y, z);
    }

    @Override
    public IMessage onMessage(PacketCropRequest msg, MessageContext ctx) {
        HFTrackers.getCropTracker().sendUpdateToClient(ctx.getServerHandler().playerEntity, getWorld(msg.dimension), msg.pos);
        return null;
    }
}