package joshie.harvest.player.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncBirthday extends PenguinPacket {
    private int day;
    private Season season;
    private int year;

    public PacketSyncBirthday() {}
    public PacketSyncBirthday(CalendarDate date) {
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
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getClientPlayerTracker().getStats().setBirthday(new CalendarDate(day, season, year));
    }
}