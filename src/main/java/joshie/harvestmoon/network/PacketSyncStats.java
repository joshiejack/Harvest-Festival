package joshie.harvestmoon.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.helpers.ClientHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncStats implements IMessage, IMessageHandler<PacketSyncStats, IMessage> {
    private double stamina;
    private double fatigue;
    private double staminaMax;
    private double fatigueMin;
    
    public PacketSyncStats() {}
    public PacketSyncStats(double stamina, double fatigue, double staminaMax, double fatigueMin) {
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.staminaMax = staminaMax;
        this.fatigueMin = fatigueMin;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(stamina);
        buf.writeDouble(fatigue);
        buf.writeDouble(staminaMax);
        buf.writeDouble(fatigueMin);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stamina = buf.readDouble();
        fatigue = buf.readDouble();
        staminaMax = buf.readDouble();
        fatigueMin = buf.readDouble();
    }
    
    @Override
    public IMessage onMessage(PacketSyncStats message, MessageContext ctx) {        
        ClientHelper.getPlayerData().setStats(message.stamina, message.fatigue, message.staminaMax, message.fatigueMin);

        return null;
    }
}