package joshie.harvest.core.network.quests;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.quests.Quest;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
            ByteBufUtils.writeUTF8String(buf, quest.getUniqueName());
            quest.toBytes(buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        boolean isNull = buf.readBoolean();
        if (!isNull) {
            IQuest q = HFApi.QUESTS.get(ByteBufUtils.readUTF8String(buf));

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