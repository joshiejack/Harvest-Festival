package joshie.harvest.player.packet;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PacketNBT;
import net.minecraft.entity.player.EntityPlayer;

@Packet(Packet.Side.CLIENT)
public class PacketSyncStatusReset extends PacketNBT {
    public PacketSyncStatusReset() {}
    public PacketSyncStatusReset(CalendarDate yesterday) {
        super(yesterday.toNBT());
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getClientPlayerTracker().getRelationships().newDay(CalendarDate.fromNBT(tag), HFApi.calendar.getDate(player.world));
    }
}