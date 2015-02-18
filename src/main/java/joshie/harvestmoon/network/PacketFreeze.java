package joshie.harvestmoon.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.config.NPC;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketFreeze implements IMessage, IMessageHandler<PacketFreeze, IMessage> {
    public PacketFreeze() {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(PacketFreeze message, MessageContext ctx) {
        NPC.FREEZE_NPC = !NPC.FREEZE_NPC;
        return null;
    }
}