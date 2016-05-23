package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncForecast extends AbstractPacketDimension {
    private Weather[] forecast;

    public PacketSyncForecast() {}

    public PacketSyncForecast(int dimension, Weather[] forecast) {
        super(dimension);
        this.forecast = forecast;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        for (int i = 0; i < 7; i++) {
            buf.writeByte(forecast[i].ordinal());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        forecast = new Weather[7];
        for (int i = 0; i < 7; i++) {
            forecast[i] = Weather.values()[buf.readByte()];
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getCalendar(player.worldObj).setForecast(forecast);
    }
}