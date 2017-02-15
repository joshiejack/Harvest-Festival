package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.quests.base.QuestFestivalTimed;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

@HFQuest("festival.harvest.festival")
public class QuestHarvestFestival extends QuestFestivalTimed {
    public QuestHarvestFestival() {}

    @Override
    protected boolean isCorrectTime(long time) {
        return time >= 8000L && time <= 20000L;
    }

    @Override
    @Nullable
    protected String getLocalizedScript(EntityPlayer player, NPC npc) {
        return null;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onChatClosed(EntityPlayer player, NPC npc) {}
}
