package joshie.harvest.calendar.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.data.CalendarClient;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncForecast extends PenguinPacket {
    private Weather[] forecast;

    public PacketSyncForecast() {}
    public PacketSyncForecast(Weather[] forecast) {
        this.forecast = forecast;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (int i = 0; i < 7; i++) {
            buf.writeByte(forecast[i].ordinal());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        forecast = new Weather[7];
        for (int i = 0; i < 7; i++) {
            forecast[i] = Weather.values()[buf.readByte()];
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.<CalendarClient>getCalendar(player.worldObj).setForecast(forecast);
    }
}