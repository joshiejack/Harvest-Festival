package joshie.harvest.core.network.quests;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.penguin.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketQuestSetStage extends PenguinPacket {
    private IQuest quest;
    private int stage;

    public PacketQuestSetStage() {}

    public PacketQuestSetStage(IQuest quest, int stage) {
        this.quest = quest;
        this.stage = stage;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(stage);
        ByteBufUtils.writeUTF8String(buf, quest.getUniqueName());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stage = buf.readShort();
        quest = HFApi.quests.get(ByteBufUtils.readUTF8String(buf));
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