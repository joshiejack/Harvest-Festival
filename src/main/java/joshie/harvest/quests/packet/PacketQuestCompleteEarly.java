package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.api.quests.QuestQuestion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.CLIENT)
public class PacketQuestCompleteEarly extends PenguinPacket {
    private Quest quest;

    public PacketQuestCompleteEarly() {}

    public PacketQuestCompleteEarly(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = Quest.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        QuestQuestion real = (QuestQuestion) HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getAQuest(quest);
        if (real != null) {
            real.isCompletedEarly = true;
        }
    }
}