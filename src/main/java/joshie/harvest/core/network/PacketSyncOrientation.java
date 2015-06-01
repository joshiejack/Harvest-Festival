package joshie.harvest.core.network;

import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncOrientation extends AbstractPacketOrientation implements IMessageHandler<PacketSyncOrientation, IMessage> {
    
    public PacketSyncOrientation() {}
    public PacketSyncOrientation(int dim, int x, int y, int z, ForgeDirection dir) {
        super(dim, x, y, z, dir);
    }
    
    @Override
    public IMessage onMessage(PacketSyncOrientation message, MessageContext ctx) {
        return super.onMessage(message, ctx);
    }
}