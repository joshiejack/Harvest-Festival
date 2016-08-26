package joshie.harvest.quests.tutorial;

import joshie.harvest.api.HFQuest;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.core.lib.HFQuests.TUTORIAL_INTRO;
import static joshie.harvest.npc.HFNPCs.*;
import static joshie.harvest.quests.QuestHelper.rewardGold;

@HFQuest(data = "tutorial.carpenter")
public class QuestCarpenter extends Quest {
    public QuestCarpenter() {
        setNPCs(GODDESS, BUILDER, SEED_OWNER);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_INTRO);
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == 0) {

        }

        return null;
    }

    @Override
    public void claim(EntityPlayer player) {
        rewardGold(player, 1000);
    }
}