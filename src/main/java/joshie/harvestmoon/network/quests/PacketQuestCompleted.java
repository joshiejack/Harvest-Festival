package joshie.harvestmoon.network.quests;

import static cpw.mods.fml.common.network.ByteBufUtils.readUTF8String;
import static cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String;
import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.helpers.QuestHelper;
import joshie.harvestmoon.helpers.generic.MCClientHelper;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.quests.Quest;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestCompleted implements IMessage, IMessageHandler<PacketQuestCompleted, IMessage> {
    private Quest quest;
    private boolean isSenderClient;

    public PacketQuestCompleted() {}

    public PacketQuestCompleted(Quest quest, boolean isSenderClient) {
        this.isSenderClient = isSenderClient;
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isSenderClient);
        writeUTF8String(buf, quest.getName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isSenderClient = buf.readBoolean();
        quest = HMQuests.get(readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketQuestCompleted message, MessageContext ctx) {
        if (message.isSenderClient) {
            QuestHelper.markCompleted(ctx.getServerHandler().playerEntity, message.quest);
        } else {
            QuestHelper.markCompleted(MCClientHelper.getPlayer(), message.quest);
        }

        return null;
    }
}