package joshie.harvestmoon.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.core.handlers.events.FMLEvents;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketNewDay implements IMessage, IMessageHandler<PacketNewDay, IMessage> {
    public PacketNewDay() {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(PacketNewDay message, MessageContext ctx) {
        FMLEvents.newDay(true);
        return null;
    }
}