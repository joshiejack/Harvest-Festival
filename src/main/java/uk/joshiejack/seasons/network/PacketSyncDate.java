package uk.joshiejack.seasons.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.seasons.client.WorldDataClient;
import uk.joshiejack.seasons.date.CalendarDate;

import java.time.DayOfWeek;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncDate extends PenguinPacket {
    private CalendarDate date;

    public PacketSyncDate() {}
    public PacketSyncDate(CalendarDate date) {
        this.date = date;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeByte(date.getWeekday().ordinal());
        to.writeShort(date.getDay());
        to.writeShort(date.getYear());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        date = new CalendarDate(DayOfWeek.values()[from.readByte()], from.readShort(), from.readShort());
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        WorldDataClient.INSTANCE.date = date; //Client side
    }
}
