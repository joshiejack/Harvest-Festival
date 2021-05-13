package uk.joshiejack.penguinlib.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public abstract class PenguinPacket implements IMessage {
    public boolean handleServerPacket(EntityPlayerMP player) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void handleClientPacket(NetHandlerPlayClient handler) {
        handlePacket(Minecraft.getMinecraft().player);
    }

    public void handleServerPacket(NetHandlerPlayServer serverHandler) {
        if (!handleServerPacket(serverHandler.player)) {
            handlePacket(serverHandler.player);
        }
    }

    public void handlePacket(EntityPlayer player) {}
}
