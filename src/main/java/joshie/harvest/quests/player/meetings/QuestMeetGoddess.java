package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.quests.selection.TutorialSelection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.npcs.HFNPCs.GODDESS;
import static joshie.harvest.quests.Quests.YULIF_MEET;

@HFQuest("tutorial.intro")
public class QuestMeetGoddess extends QuestQuestion {
    private static final int HELLO = 0;
    private static final int BACKSTORY = 1;

    public QuestMeetGoddess() {
        super(new TutorialSelection("intro"));
        setNPCs(GODDESS);
    }

    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPC npc) {
        //The goddess says hello and asks if you are new
        if (isCompletedEarly()) {
            return getLocalized("completed");
        } else if (quest_stage == HELLO) return getLocalized("hello");
        else if (quest_stage == BACKSTORY) {
            //The goddess gives you a back story about the world, she then lets the player
            //know that she will despawn after a while, but will leave a goddess flower behind
            //most of the time, so that you can resummon here
            return getLocalized("backstory");
        } else return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPC npc) {
        if (quest_stage == BACKSTORY) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        if (isCompletedEarly()) {
            HFApi.quests.completeQuest(YULIF_MEET, player);
            HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().learnNote(HFNotes.BLUEPRINTS);
        }

        rewardItem(player, new ItemStack(HFCore.FLOWERS, 4, 0));
    }
}
