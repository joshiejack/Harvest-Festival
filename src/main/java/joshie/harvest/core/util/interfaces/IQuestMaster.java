package joshie.harvest.core.util.interfaces;

import joshie.harvest.api.quests.QuestType;
import joshie.harvest.quests.packet.PacketQuest;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public interface IQuestMaster {
    QuestType getQuestType();

    /** Sync to everyone of if applicable only the player passed in **/
    void sync(@Nullable EntityPlayer player, PacketQuest packet);
}
