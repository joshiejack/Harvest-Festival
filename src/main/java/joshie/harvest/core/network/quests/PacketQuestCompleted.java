package joshie.harvest.core.network.quests;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestCompleted implements IMessage, IMessageHandler<PacketQuestCompleted, IMessage> {
    private IQuest quest;
    private boolean isSenderClient;

    public PacketQuestCompleted() {}

    public PacketQuestCompleted(IQuest quest, boolean isSenderClient) {
        this.isSenderClient = isSenderClient;
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isSenderClient);
        ByteBufUtils.writeUTF8String(buf, quest.getUniqueName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isSenderClient = buf.readBoolean();
        quest = HFApi.QUESTS.get(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketQuestCompleted message, MessageContext ctx) {
        EntityPlayer player = null;
        if(message.isSenderClient) {
            player = MCClientHelper.getPlayer();
        } else player = ctx.getServerHandler().playerEntity;
        
        HFTrackers.getPlayerTracker(player).getQuests().markCompleted(message.quest, false);
        return null;
    }
}