package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.core.ICalendarDate;
import joshie.harvest.api.core.Season;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncBirthday implements IMessage, IMessageHandler<PacketSyncBirthday, IMessage> {
    private int day;
    private Season season;
    private int year;

    public PacketSyncBirthday() {}
    public PacketSyncBirthday(ICalendarDate date) {
        this.day = date.getDay();
        this.season = date.getSeason();
        this.year = date.getYear();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(day);
        buf.writeByte(season.ordinal());
        buf.writeShort(year);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        day = buf.readByte();
        season = Season.values()[buf.readByte()];
        year = buf.readShort();
    }

    @Override
    public IMessage onMessage(PacketSyncBirthday message, MessageContext ctx) {
        PlayerHelper.setBirthday(MCClientHelper.getPlayer(), message.day, message.season, message.year);

        return null;
    }
}