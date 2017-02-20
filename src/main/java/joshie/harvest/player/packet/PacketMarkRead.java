package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.SERVER)
public class PacketMarkRead extends PenguinPacket {
    private ResourceLocation resource;

    public PacketMarkRead() { }
    public PacketMarkRead(ResourceLocation resource) {
        this.resource = resource;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, resource.toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        resource = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().getReadStatus().add(resource);
    }
}