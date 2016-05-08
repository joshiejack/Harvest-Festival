package joshie.harvest.core.network;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class PacketSyncOrientation extends AbstractPacketOrientation {
    public PacketSyncOrientation() {}
    public PacketSyncOrientation(int dim, BlockPos pos, EnumFacing dir) {
        super(dim, pos, dir);
    }
}