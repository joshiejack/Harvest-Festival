package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.quests.base.QuestFestivalTimed;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

@HFQuest("festival.sheep")
public class QuestContestSheep extends QuestFestivalTimed {
    public QuestContestSheep() {}

    @Override //If the npc is a marriage candidate, we can process them for this festival
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return entity.getNPC().getAge() == Age.ADULT;
    }

    @Override
    protected boolean isCorrectTime(long time) {
        return time >= 6000L && time <= 18000L;
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
