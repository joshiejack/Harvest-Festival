package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.config.NPC;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFreeze implements IMessage, IMessageHandler<PacketFreeze, IMessage> {
    public PacketFreeze() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public IMessage onMessage(PacketFreeze message, MessageContext ctx) {
        NPC.FREEZE_NPC = true;
        return null;
    }
}