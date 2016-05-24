package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSetCalendar extends AbstractPacketDimension {
    private int day;
    private Season season;
    private int year;
    
    public PacketSetCalendar() {}
    public PacketSetCalendar(int dimension, ICalendarDate date) {
        super(dimension);
        this.day = date.getDay();
        this.season = date.getSeason();
        this.year = date.getYear();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(day);
        buf.writeByte(season.ordinal());
        buf.writeInt(year);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        day = buf.readInt();
        season = Season.values()[buf.readByte()];
        year = buf.readInt();
    }
    
    @Override
    public void handlePacket(EntityPlayer player) {
        ICalendarDate date = HFTrackers.getCalendar(player.worldObj).getDate();
        Season previous = date.getSeason();
        date.setDay(day).setSeason(season).setYear(year);

        //Refresh all Blocks in Render range
        //If the seasons are not the same, re-render the client
        if (previous != season) {
            joshie.harvest.core.helpers.generic.MCClientHelper.refresh();
        }
    }
}