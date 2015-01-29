package joshie.harvestmoon.network.quests;

import static cpw.mods.fml.common.network.ByteBufUtils.readUTF8String;
import static cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String;
import static joshie.harvestmoon.HarvestMoon.handler;
import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.quests.Quest;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestSetCurrent implements IMessage, IMessageHandler<PacketQuestSetCurrent, IMessage> {
    private Quest quest;

    public PacketQuestSetCurrent() {}

    public PacketQuestSetCurrent(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(quest == null);
        if (quest != null) {
            writeUTF8String(buf, quest.getName());
            quest.toBytes(buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        boolean isNull = buf.readBoolean();
        if (!isNull) {
            Quest q = HMQuests.get(readUTF8String(buf));

            try {
                quest = ((Quest) q.getClass().newInstance()).setName(q.getName());
                quest.fromBytes(buf);
            } catch (Exception e) {}
        }
    }

    @Override
    public IMessage onMessage(PacketQuestSetCurrent message, MessageContext ctx) {
        handler.getClient().getPlayerData().getQuests().addAsCurrent(message.quest);
        return null;
    }
}