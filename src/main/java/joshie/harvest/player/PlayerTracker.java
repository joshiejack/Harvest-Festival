package joshie.harvest.player;

import joshie.harvest.knowledge.letter.LetterData;
import joshie.harvest.player.relationships.RelationshipData;
import joshie.harvest.player.stats.Stats;
import joshie.harvest.player.tracking.Tracking;
import joshie.harvest.quests.data.QuestData;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public abstract class PlayerTracker {
    public abstract EntityPlayer getAndCreatePlayer();
    public abstract UUID getUUID();
    public abstract QuestData getQuests();
    public abstract RelationshipData getRelationships();
    public abstract Stats getStats();
    public abstract Tracking getTracking();
    public abstract LetterData getLetters();
}
