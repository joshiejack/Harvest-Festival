package joshie.harvest.core.network.quests;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.penguin.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketQuestSetAvailable extends PenguinPacket {
    private IQuest quest;

    public PacketQuestSetAvailable() {}
    public PacketQuestSetAvailable(IQuest quest) {
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
        QuestHelper.markAvailable(MCClientHelper.getPlayer(), quest);
    }
}