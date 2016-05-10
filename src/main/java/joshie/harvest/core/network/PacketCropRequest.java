package joshie.harvest.core.network;

import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.generic.MCServerHelper.getWorld;

public class PacketCropRequest extends AbstractPacketLocation {
    public PacketCropRequest() { }
    public PacketCropRequest(World world, BlockPos pos) {
        super(world.provider.getDimension(), pos);
    }

    @Override
    public boolean handleServerPacket(EntityPlayerMP player) {
        HFTrackers.getCropTracker().sendUpdateToClient(player, getWorld(dim), pos);
        return true;
    }
}