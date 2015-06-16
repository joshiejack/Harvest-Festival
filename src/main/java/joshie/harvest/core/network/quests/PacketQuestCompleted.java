package joshie.harvest.core.network.quests;

import static cpw.mods.fml.common.network.ByteBufUtils.readUTF8String;
import static cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String;
import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTracker;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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
        writeUTF8String(buf, quest.getUniqueName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isSenderClient = buf.readBoolean();
        quest = HFApi.QUESTS.get(readUTF8String(buf));
    }

    @Override
    public IMessage onMessage(PacketQuestCompleted message, MessageContext ctx) {
        EntityPlayer player = null;
        if(message.isSenderClient) {
            player = MCClientHelper.getPlayer();
        } else player = ctx.getServerHandler().playerEntity;
        
        HFTracker.getPlayerTracker(player).getQuests().markCompleted(message.quest, false);
        return null;
    }
}