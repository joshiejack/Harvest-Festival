package joshie.harvest.quests.player.tutorial;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.core.HFCore;
import joshie.harvest.quests.QuestHelper;
import joshie.harvest.quests.selection.TutorialSelection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.npc.HFNPCs.GODDESS;
import static joshie.harvest.quests.Quests.TUTORIAL_CARPENTER;

@HFQuest("tutorial.intro")
public class QuestIntro extends QuestQuestion {
    private static final int HELLO = 0;
    private static final int BACKSTORY = 1;

    public QuestIntro() {
        super(new TutorialSelection("intro"));
        setNPCs(GODDESS);
    }

    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        //The goddess says hello and asks if you are new
        if (isCompletedEarly) {
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
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean isSneaking) {
        if (isCompletedEarly) QuestHelper.INSTANCE.completeQuest(TUTORIAL_CARPENTER, player);
        if (quest_stage == BACKSTORY || isCompletedEarly) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, new ItemStack(HFCore.FLOWERS, 4, 0));
    }
}
