package joshie.harvest.api.player;

import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;

/** Helper interface for dealing with player data **/
public interface IPlayerHelper {
    /** Get the relationship helper **/
    IRelationships getRelationshipHelper();

    /** Sync quest data, Ideally you'll never need to call this,
     *  It's called from Quest only, and should remain so */
    void syncQuest(Quest quest, EntityPlayer player);
}
