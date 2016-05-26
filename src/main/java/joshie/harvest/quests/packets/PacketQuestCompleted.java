package joshie.harvest.quests.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.penguin.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketQuestCompleted extends PenguinPacket {
    private IQuest quest;

    public PacketQuestCompleted() {}

    public PacketQuestCompleted(IQuest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, quest.getUniqueName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = HFApi.quests.get(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getPlayerTracker(player).getQuests().markCompleted(quest, false);
    }
}