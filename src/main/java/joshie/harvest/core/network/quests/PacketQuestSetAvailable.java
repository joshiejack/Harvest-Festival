package joshie.harvest.core.network.quests;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestSetAvailable implements IMessage, IMessageHandler<PacketQuestSetAvailable, IMessage> {
    private IQuest quest;

    public PacketQuestSetAvailable() {}
    public PacketQuestSetAvailable(IQuest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, quest.getUniqueName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = HFApi.QUESTS.get(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketQuestSetAvailable message, MessageContext ctx) {
        QuestHelper.markAvailable(MCClientHelper.getPlayer(), message.quest);
        return null;
    }
}