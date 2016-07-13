package joshie.harvest.api.player;

import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;

public interface IQuestHelper {
    /** Increases the stage of the quest
     *  @param quest    the quest to sync
     *  @param player   the player syncing to*/
    void increaseStage(Quest quest, EntityPlayer player);

    /** Completes a quest
     *  @param quest    the quest to mark as completed
     *  @param player   the player to complete the quest for**/
    void completeQuest(Quest quest, EntityPlayer player);
}
