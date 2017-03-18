package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

//TODO: Readd in 1.0 as well as adding elevators to the mine on every 5th floor
//@Packet(Packet.Side.CLIENT)
@SuppressWarnings("unused")
public class PacketSyncMineFloor extends PenguinPacket {
    private int floor;

    public PacketSyncMineFloor() { }
    public PacketSyncMineFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(floor);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        floor = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getClientPlayerTracker().getTracking().setMineFloorReached(floor);
    }
}