package joshie.harvest.core.network;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncOrientation extends AbstractPacketOrientation implements IMessageHandler<PacketSyncOrientation, IMessage> {
    
    public PacketSyncOrientation() {}
    public PacketSyncOrientation(int dim, BlockPos pos, EnumFacing dir) {
        super(dim, pos, dir);
    }
    
    @Override
    public IMessage onMessage(PacketSyncOrientation message, MessageContext ctx) {
        return super.onMessage(message, ctx);
    }
}