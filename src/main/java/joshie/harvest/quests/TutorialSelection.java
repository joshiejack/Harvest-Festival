package joshie.harvest.quests;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.quests.QuestQuestion.QuestSelection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class TutorialSelection extends QuestSelection<QuestQuestion> {
    public TutorialSelection(String name) {
        super("tutorial." + name + ".question", "tutorial." + name + ".yes", "tutorial." + name + ".no");
    }

    @Override
    public Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, QuestQuestion quest, int option) {
        if (option == 1) { //If it's our first time, start tutorials
            quest.increaseStage(player);
            return Result.ALLOW;
        } else { //If it's not then give the player the essentials to get started
            quest.complete(player);
            return Result.DENY;
        }
    }
}
