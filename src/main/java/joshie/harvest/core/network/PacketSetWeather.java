package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.core.handlers.HFTrackers;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSetWeather implements IMessage, IMessageHandler<PacketSetWeather, IMessage> {
    private Weather weather;
    
    public PacketSetWeather() {}
    public PacketSetWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(weather.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        weather = Weather.values()[buf.readByte()];
    }
    
    @Override
    public IMessage onMessage(PacketSetWeather message, MessageContext ctx) {  
        HFTrackers.getCalendar().setTodaysWeather(message.weather);
        return null;
    }
}