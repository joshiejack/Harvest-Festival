package harvestmoon.network;

import static harvestmoon.HarvestMoon.handler;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncStats implements IMessage, IMessageHandler<PacketSyncStats, IMessage> {
    private double stamina;
    private double fatigue;
    
    public PacketSyncStats() {}
    public PacketSyncStats(double stamina, double fatigue) {
        this.stamina = stamina;
        this.fatigue = fatigue;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(stamina);
        buf.writeDouble(fatigue);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stamina = buf.readDouble();
        fatigue = buf.readDouble();
    }
    
    @Override
    public IMessage onMessage(PacketSyncStats message, MessageContext ctx) {        
        handler.getClient().getPlayerData().setStats(message.stamina, message.fatigue);

        return null;
    }
}