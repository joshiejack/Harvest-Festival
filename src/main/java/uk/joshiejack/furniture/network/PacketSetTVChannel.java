package uk.joshiejack.furniture.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.furniture.television.TVChannel;
import uk.joshiejack.furniture.tile.TileTelevision;
import uk.joshiejack.penguinlib.network.packet.PacketSendPenguin;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.SERVER)
public class PacketSetTVChannel extends PacketSendPenguin<TVChannel> {
    private BlockPos pos;

    public PacketSetTVChannel() { super(TVChannel.REGISTRY); }
    public PacketSetTVChannel(BlockPos pos, TVChannel channel) {
        super(TVChannel.REGISTRY, channel);
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        to.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        pos = BlockPos.fromLong(from.readLong());
    }

    @Override
    public void handlePacket(EntityPlayer player, TVChannel channel) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileTelevision) {
            TileTelevision television = ((TileTelevision)tile);
            if (!player.world.isRemote) {
                Scripting.callFunction(channel.getScript(), "watch", player, television);
            }
        }
    }
}

