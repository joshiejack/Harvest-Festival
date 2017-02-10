package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.quests.Quests.JOHAN_MEET;

@HFQuest("meeting.tiberius")
public class QuestMeetTiberius extends QuestMeeting {
    public QuestMeetTiberius() {
        super(HFBuildings.CLOCKMAKER, HFNPCs.CLOCKMAKER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JOHAN_MEET);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, new ItemStack(Items.CLOCK));
    }
}
