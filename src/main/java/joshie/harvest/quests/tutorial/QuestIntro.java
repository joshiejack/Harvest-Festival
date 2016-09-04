package joshie.harvest.quests.tutorial;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.core.HFCore;
import joshie.harvest.quests.QuestQuestion;
import joshie.harvest.quests.TutorialSelection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest("tutorial.intro")
public class QuestIntro extends QuestQuestion {
    public QuestIntro() {
        super(new TutorialSelection("intro"));
        setNPCs(GODDESS);
    }

    @SideOnly(Side.CLIENT)
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        //The goddess says hello and asks if you are new
        if (isCompleted) {
            complete(player);
            return "completed";
        } else  if (quest_stage == 0) return "hello";
        else if (quest_stage == 1) {
            //The goddess gives you a back story about the world, she then lets the player
            //know that she will despawn after a while, but will leave a goddess flower behind
            //most of the time, so that you can resummon here
            complete(player);
            return "backstory";
        } else return null;
    }

    @Override
    public void claim(EntityPlayer player) {
        rewardItem(player, new ItemStack(HFCore.FLOWERS, 4, 0));
    }

    private static class IntroSelection extends QuestSelection<QuestIntro> {
        public IntroSelection() {
            super("tutorial.intro.question", "tutorial.intro.yes", "tutorial.intro.no");
        }

        @Override
        public Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, QuestIntro quest, int option) {
            if (option == 1) { //If it's our first time, start tutorials
                quest.increaseStage(player);
                return Result.ALLOW;
            } else { //If it's not then give the player the essentials to get started
                quest.complete(player);
                return Result.DENY;
            }
        }
    }
}
