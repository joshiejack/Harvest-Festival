package joshie.harvest.core.network;

import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.core.helpers.generic.MCServerHelper.getWorld;

public class PacketCropRequest extends AbstractPacketLocation {
    public PacketCropRequest() { }
    public PacketCropRequest(int dimension, BlockPos pos) {
        super(dimension, pos);
    }

    @Override
    public boolean handleServerPacket(EntityPlayerMP player) {
        HFTrackers.getCropTracker(getWorld(dim)).sendUpdateToClient(player, pos);
        return true;
    }
}