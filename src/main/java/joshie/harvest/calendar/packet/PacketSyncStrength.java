package joshie.harvest.calendar.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncStrength extends PenguinPacket {
    private float rain;
    private float storm;

    public PacketSyncStrength() {}
    public PacketSyncStrength(float rain, float storm) {
        this.rain = rain;
        this.storm = storm;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(rain);
        buf.writeFloat(storm);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        rain = buf.readFloat();
        storm = buf.readFloat();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.<CalendarClient>getCalendar(player.worldObj).setStrength(rain, storm);
    }
}