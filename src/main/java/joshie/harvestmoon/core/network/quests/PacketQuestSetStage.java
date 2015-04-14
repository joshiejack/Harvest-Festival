package joshie.harvestmoon.core.network.quests;

import static cpw.mods.fml.common.network.ByteBufUtils.readUTF8String;
import static cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String;
import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.quest.IQuest;
import joshie.harvestmoon.core.helpers.QuestHelper;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketQuestSetStage implements IMessage, IMessageHandler<PacketQuestSetStage, IMessage> {
    private IQuest quest;
    private boolean isSenderClient;
    private int stage;

    public PacketQuestSetStage() {}

    public PacketQuestSetStage(IQuest quest, boolean isSenderClient, int stage) {
        this.quest = quest;
        this.isSenderClient = isSenderClient;
        this.stage = stage;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isSenderClient);
        buf.writeShort(stage);
        writeUTF8String(buf, quest.getUniqueName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isSenderClient = buf.readBoolean();
        stage = buf.readShort();
        quest = HMApi.QUESTS.get(readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketQuestSetStage message, MessageContext ctx) {
        if (message.isSenderClient) {
            QuestHelper.setQuestStage(ctx.getServerHandler().playerEntity, message.quest, message.stage);
        } else {
            QuestHelper.setQuestStage(MCClientHelper.getPlayer(), message.quest, message.stage);
        }

        return null;
    }
}