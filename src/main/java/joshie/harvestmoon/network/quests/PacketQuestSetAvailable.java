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

public class PacketQuestSetAvailable implements IMessage, IMessageHandler<PacketQuestSetAvailable, IMessage> {
    private Quest quest;

    public PacketQuestSetAvailable() {}

    public PacketQuestSetAvailable(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeUTF8String(buf, quest.getName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = HMQuests.get(readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketQuestSetAvailable message, MessageContext ctx) {
        handler.getClient().getPlayerData().getQuests().setAvailable(message.quest);
        return null;
    }
}