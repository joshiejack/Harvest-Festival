package joshie.harvestmoon.network;

import static joshie.harvestmoon.HarvestMoon.handler;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncGold implements IMessage, IMessageHandler<PacketSyncGold, IMessage> {
    private long gold;
    
    public PacketSyncGold() {}
    public PacketSyncGold(long gold) {
        this.gold = gold;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(gold);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gold = buf.readLong();
    }
    
    @Override
    public IMessage onMessage(PacketSyncGold message, MessageContext ctx) {        
        handler.getClient().getPlayerData().setGold(message.gold);

        return null;
    }
}