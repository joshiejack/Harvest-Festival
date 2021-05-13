package uk.joshiejack.penguinlib.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("WeakerAccess")
public class PenguinHandler implements IMessageHandler<PenguinPacket, IMessage> {
    @Override
    public IMessage onMessage(final PenguinPacket message, final MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(() -> message.handleClientPacket(ctx.getClientHandler()));
        } else {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> message.handleServerPacket(ctx.getServerHandler()));
        }

        return null;
    }
}
