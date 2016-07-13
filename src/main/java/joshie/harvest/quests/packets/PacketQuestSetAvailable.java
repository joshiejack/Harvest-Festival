package joshie.harvest.quests.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.penguin.PenguinPacket;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

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
        QuestHelper.markAvailable(MCClientHelper.getPlayer(), quest);
    }
}