package joshie.harvest.quests.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Packet.Side.CLIENT)
public class PacketQuestSetAvailable extends PenguinPacket {
    private Quest quest;

    public PacketQuestSetAvailable() {}
    public PacketQuestSetAvailable(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest = Quest.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        QuestHelper.markAvailable(player, quest);
    }
}