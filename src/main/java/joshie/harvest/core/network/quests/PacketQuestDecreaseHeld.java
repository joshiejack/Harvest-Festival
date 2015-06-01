package joshie.harvest.core.network.quests;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestDecreaseHeld implements IMessage, IMessageHandler<PacketQuestDecreaseHeld, IMessage> {
    private int amount;
    
    public PacketQuestDecreaseHeld() {}
    public PacketQuestDecreaseHeld(int amount) {
        this.amount = amount;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(amount);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        amount = buf.readByte();
    }

    @Override
    public IMessage onMessage(PacketQuestDecreaseHeld message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        player.inventory.decrStackSize(player.inventory.currentItem, message.amount);
        return null;
    }
}