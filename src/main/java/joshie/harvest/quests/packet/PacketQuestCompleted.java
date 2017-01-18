package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.CLIENT)
public class PacketQuestCompleted extends PacketQuest {
    private Quest quest;
    private boolean rewards;

    @SuppressWarnings("unused")
    public PacketQuestCompleted() {}
    public PacketQuestCompleted(Quest quest, boolean rewards) {
        this.quest = quest;
        this.rewards = rewards;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
        buf.writeBoolean(rewards);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        quest = Quest.REGISTRY.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        rewards = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        getQuestDataFromPlayer(player).markCompleted(player.worldObj, player, quest, rewards);
    }
}