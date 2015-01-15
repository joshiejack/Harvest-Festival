package joshie.harvestmoon.network.quests;

import static cpw.mods.fml.common.network.ByteBufUtils.readUTF8String;
import static cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String;
import static joshie.harvestmoon.HarvestMoon.handler;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.quests.Quest;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestStart implements IMessage, IMessageHandler<PacketQuestStart, IMessage> {
    private Quest quest;

    public PacketQuestStart() {}

    public PacketQuestStart(Quest quest) {
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
    public IMessage onMessage(PacketQuestStart message, MessageContext ctx) {
        handler.getServer().getPlayerData(ctx.getServerHandler().playerEntity).getQuests().startQuest(message.quest);
        return null;
    }
}