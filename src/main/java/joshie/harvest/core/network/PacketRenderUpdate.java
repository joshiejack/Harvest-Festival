package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class PacketRenderUpdate extends AbstractPacketLocation {
    public PacketRenderUpdate() {}

    public PacketRenderUpdate(int dim, BlockPos pos) {
        super(dim, pos);
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
    }

    public void handlePacket(EntityPlayer player) {
        MCClientHelper.updateRender(pos);
    }
}