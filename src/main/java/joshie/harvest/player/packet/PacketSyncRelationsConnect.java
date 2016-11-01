package joshie.harvest.player.packet;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PacketNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

@Packet(Packet.Side.CLIENT)
public class PacketSyncRelationsConnect extends PacketNBT {
    public PacketSyncRelationsConnect() {}
    public PacketSyncRelationsConnect(NBTTagCompound tag) {
        super(tag);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getClientPlayerTracker().getRelationships().readFromNBT(tag);
    }
}