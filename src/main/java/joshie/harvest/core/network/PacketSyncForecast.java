package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.core.handlers.DataHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncForecast implements IMessage, IMessageHandler<PacketSyncForecast, IMessage> {
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
    public IMessage onMessage(PacketSyncForecast message, MessageContext ctx) {  
        DataHelper.getCalendar().setForecast(message.forecast);
        return null;
    }
}