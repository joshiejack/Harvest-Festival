package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class PenguinPacket implements IMessage {
    public void handlePacket(EntityPlayer player) {}

    public boolean handleServerPacket(EntityPlayerMP player) {
        return false;
    }

    @Override
    public void toBytes(ByteBuf to) {}

    @Override
    public void fromBytes(ByteBuf from) {}

    public void handleQueuedClient(NetHandlerPlayClient handler) {
        handlePacket(MCClientHelper.getPlayer());
    }

    public void handleQueuedServer(NetHandlerPlayServer serverHandler) {
        if (!handleServerPacket(serverHandler.playerEntity)) {
            handlePacket(serverHandler.playerEntity);
        }
    }
}