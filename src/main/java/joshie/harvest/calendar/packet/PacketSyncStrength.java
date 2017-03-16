package joshie.harvest.calendar.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.calendar.data.CalendarClient;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncStrength extends PenguinPacket {
    private int rain;
    private int storm;

    public PacketSyncStrength() {}
    public PacketSyncStrength(int rain, int storm) {
        this.rain = rain;
        this.storm = storm;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(rain);
        buf.writeInt(storm);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        rain = buf.readInt();
        storm = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.<CalendarClient>getCalendar(player.worldObj).setStrength(rain, storm);
    }
}