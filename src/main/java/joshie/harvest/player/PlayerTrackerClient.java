package joshie.harvest.player;

import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.knowledge.letter.LetterDataClient;
import joshie.harvest.player.relationships.RelationshipDataClient;
import joshie.harvest.player.stats.StatsClient;
import joshie.harvest.player.tracking.TrackingClient;
import joshie.harvest.quests.data.QuestDataClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class PlayerTrackerClient extends PlayerTracker {
    private final LetterDataClient letters = new LetterDataClient();
    private final QuestDataClient quests = new QuestDataClient();
    private final RelationshipDataClient relationships = new RelationshipDataClient();
    private final StatsClient stats = new StatsClient();
    private final TrackingClient tracking = new TrackingClient();

    @Override
    public EntityPlayer getAndCreatePlayer() {
        return MCClientHelper.getPlayer();
    }

    @Override
    public UUID getUUID() {
        return EntityHelper.getPlayerUUID(getAndCreatePlayer());
    }

    @Override
    public LetterDataClient getLetters() {
        return letters;
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
    public StatsClient getStats() {
        return stats;
    }

    @Override
    public TrackingClient getTracking() {
        return tracking;
    }
}
