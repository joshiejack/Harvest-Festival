package joshie.harvestmoon.network;

import static joshie.harvestmoon.HarvestMoon.handler;
import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketGoldCommand implements IMessage, IMessageHandler<PacketGoldCommand, IMessage> {
    private boolean set;
    private int gold;

    public PacketGoldCommand() {}

    public PacketGoldCommand(int gold, boolean set) {
        this.gold = gold;
        this.set = set;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(gold);
        buf.writeBoolean(set);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gold = buf.readInt();
        set = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(PacketGoldCommand message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (message.set) {
            handler.getServer().getPlayerData(player).setGold(message.gold);
            PacketHandler.sendToClient(new PacketSyncGold(PlayerHelper.getGold(player)), (EntityPlayerMP) player);
        } else PlayerHelper.adjustGold(player, message.gold);

        return null;
    }
}