package joshie.harvest.player;

import joshie.harvest.player.quests.QuestData;
import joshie.harvest.player.relationships.RelationshipData;
import joshie.harvest.player.stats.StatData;
import joshie.harvest.player.tracking.TrackingData;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public abstract class PlayerTracker {
    public abstract EntityPlayer getAndCreatePlayer();

    public abstract UUID getUUID();

    public abstract QuestData getQuests();

    public abstract RelationshipData getRelationships();

    public abstract StatData getStats();

    public abstract TrackingData getTracking();
}
