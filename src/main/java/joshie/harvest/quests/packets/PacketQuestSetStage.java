package joshie.harvest.quests.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.penguin.PenguinPacket;
import joshie.harvest.quests.Quest;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketQuestSetStage extends PenguinPacket {
    private Quest quest;
    private int stage;

    public PacketQuestSetStage() {}

    public PacketQuestSetStage(Quest quest, int stage) {
        this.quest = quest;
        this.stage = stage;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(stage);
        ByteBufUtils.writeUTF8String(buf, quest.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stage = buf.readShort();
        quest = Quest.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            QuestHelper.setQuestStage(player, quest, stage);
        } else {
            QuestHelper.setQuestStage(MCClientHelper.getPlayer(), quest, stage);
        }
    }
}