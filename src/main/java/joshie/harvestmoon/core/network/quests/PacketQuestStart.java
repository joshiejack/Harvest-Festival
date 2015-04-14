package joshie.harvestmoon.core.network.quests;

import static cpw.mods.fml.common.network.ByteBufUtils.readUTF8String;
import static cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String;
import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.quest.IQuest;
import joshie.harvestmoon.core.helpers.QuestHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestStart implements IMessage, IMessageHandler<PacketQuestStart, IMessage> {
    private IQuest quest;

    public PacketQuestStart() {}

    public PacketQuestStart(IQuest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeUTF8String(buf, quest.getUniqueName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = HMApi.QUESTS.get(readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketQuestStart message, MessageContext ctx) {
        QuestHelper.startQuest(ctx.getServerHandler().playerEntity, message.quest);
        return null;
    }
}