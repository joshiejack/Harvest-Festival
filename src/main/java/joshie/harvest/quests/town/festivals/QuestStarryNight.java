package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.quests.base.QuestFestivalTimed;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

@HFQuest("festival.starry.night")
public class QuestStarryNight extends QuestFestivalTimed {
    public QuestStarryNight() {}

    @Override //If the npc is a marriage candidate, we can process them for this festival
    public boolean isNPCUsed(EntityPlayer player, NPC npc) {
        return npc.getAge() == Age.ADULT;
    }

    @Override
    protected boolean isCorrectTime(long time) {
        return time >= 13000L && time <= 22000L;
    }

    @Override
    @Nullable
    protected String getLocalizedScript(EntityPlayer player, NPC npc) {
        return null;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onChatClosed(EntityPlayer player, NPC npc) {

    }
}
