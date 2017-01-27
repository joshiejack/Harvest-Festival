package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.calendar.HolidayRegistry;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.town.data.TownDataClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

@Packet(Side.CLIENT)
public class PacketSyncFestival extends PacketSyncTown {
    private Festival festival;
    private int days;

    @SuppressWarnings("unused")
    public PacketSyncFestival(){}
    public PacketSyncFestival(UUID town, Festival festival, int days) {
        super(town);
        this.festival = festival;
        this.days = days;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, festival.toString());
        buf.writeInt(days);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        festival = HolidayRegistry.INSTANCE.getFestivalFromString(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        days = buf.readInt();
    }

    @Override
    public void handlePacket(TownDataClient town) {
        town.setFestival(festival, days);
    }
}
