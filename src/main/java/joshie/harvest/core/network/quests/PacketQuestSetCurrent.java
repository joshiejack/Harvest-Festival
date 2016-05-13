package joshie.harvest.core.network.quests;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.penguin.PenguinPacket;
import joshie.harvest.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketQuestSetCurrent extends PenguinPacket {
    private IQuest quest;

    public PacketQuestSetCurrent() {}
    public PacketQuestSetCurrent(IQuest quest) {
        this.quest = quest;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(quest == null);
        if (quest != null) {
            ByteBufUtils.writeUTF8String(buf, quest.getUniqueName());
            quest.toBytes(buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        boolean isNull = buf.readBoolean();
        if (!isNull) {
            IQuest q = HFApi.quests.get(ByteBufUtils.readUTF8String(buf));

            try {
                quest = ((Quest) q.getClass().newInstance()).setUniqueName(q.getUniqueName());
                quest.fromBytes(buf);
            } catch (Exception e) {}
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        QuestHelper.markAsCurrent(MCClientHelper.getPlayer(), quest);
    }
}