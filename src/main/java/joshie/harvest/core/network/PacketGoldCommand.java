package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketGoldCommand implements IMessage, IMessageHandler<PacketGoldCommand, IMessage> {
    private boolean set;
    private long gold;

    public PacketGoldCommand() {}

    public PacketGoldCommand(long gold, boolean set) {
        this.gold = gold;
        this.set = set;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(gold);
        buf.writeBoolean(set);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gold = buf.readLong();
        set = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(PacketGoldCommand message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (message.set) {
            PlayerHelper.setGold(player, message.gold);
        } else PlayerHelper.adjustGold(player, message.gold);

        return null;
    }
}