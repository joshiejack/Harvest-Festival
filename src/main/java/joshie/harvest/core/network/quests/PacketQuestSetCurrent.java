package joshie.harvest.core.network.quests;

import static cpw.mods.fml.common.network.ByteBufUtils.readUTF8String;
import static cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String;
import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.init.HFQuests;
import joshie.harvest.quests.Quest;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestSetCurrent implements IMessage, IMessageHandler<PacketQuestSetCurrent, IMessage> {
    private IQuest quest;

    public PacketQuestSetCurrent() {}
    public PacketQuestSetCurrent(IQuest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(quest == null);
        if (quest != null) {
            writeUTF8String(buf, quest.getUniqueName());
            quest.toBytes(buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        boolean isNull = buf.readBoolean();
        if (!isNull) {
            IQuest q = HFApi.QUESTS.get(readUTF8String(buf));

            try {
                quest = ((Quest) q.getClass().newInstance()).setUniqueName(q.getUniqueName());
                quest.fromBytes(buf);
            } catch (Exception e) {}
        }
    }

    @Override
    public IMessage onMessage(PacketQuestSetCurrent message, MessageContext ctx) {
        QuestHelper.markAsCurrent(MCClientHelper.getPlayer(), message.quest);
        return null;
    }
}