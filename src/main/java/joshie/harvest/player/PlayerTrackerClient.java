package joshie.harvest.player;

import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.player.fridge.FridgeData;
import joshie.harvest.player.quests.QuestDataClient;
import joshie.harvest.player.relationships.RelationshipDataClient;
import joshie.harvest.player.stats.StatDataClient;
import joshie.harvest.player.town.TownData;
import joshie.harvest.player.town.TownDataClient;
import joshie.harvest.player.tracking.TrackingData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class PlayerTrackerClient extends PlayerTracker {
    protected FridgeData fridge = new FridgeData();
    private QuestDataClient quests = new QuestDataClient();
    private RelationshipDataClient relationships = new RelationshipDataClient();
    private StatDataClient stats = new StatDataClient();
    private TownDataClient town = new TownDataClient();
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
    public FridgeData getFridge() {
        return fridge;
    }
    
    public void setFridge(FridgeData fridge) {
        this.fridge = fridge;
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
    public TownData getTown() {
        return town;
    }
    
    @Override
    public TrackingData getTracking() {
        return tracking;
    }

    @Override
    public EntityNPCBuilder getBuilder(World world) {
        if (builder != null) return builder;
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = (Entity) world.loadedEntityList.get(i);
            if (entity instanceof EntityNPCBuilder) {
                UUID owner = ((EntityNPCBuilder) entity).owning_player;
                if (owner == UUIDHelper.getPlayerUUID(MCClientHelper.getPlayer())) {
                    builder = (EntityNPCBuilder) entity;
                    return builder;
                }
            }
        }

        return null;
    }
}
