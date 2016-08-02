package joshie.harvest.player;

import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.player.quests.QuestDataClient;
import joshie.harvest.player.relationships.RelationshipDataClient;
import joshie.harvest.player.stats.StatDataClient;
import joshie.harvest.player.tracking.TrackingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class PlayerTrackerClient extends PlayerTracker {
    private QuestDataClient quests = new QuestDataClient();
    private RelationshipDataClient relationships = new RelationshipDataClient();
    private StatDataClient stats = new StatDataClient();
    private TrackingData tracking = new TrackingData();

    @Override
    public EntityPlayer getAndCreatePlayer() {
        return MCClientHelper.getPlayer();
    }

    @Override
    public UUID getUUID() {
        return UUIDHelper.getPlayerUUID(getAndCreatePlayer());
    }

    @Override
    public RelationshipDataClient getRelationships() {
        return relationships;
    }

    @Override
    public QuestDataClient getQuests() {
        return quests;
    }

    @Override
    public StatDataClient getStats() {
        return stats;
    }

    @Override
    public TrackingData getTracking() {
        return tracking;
    }
}
