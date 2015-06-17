package joshie.harvest.player;

import java.util.UUID;

import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.player.fridge.FridgeData;
import joshie.harvest.player.quests.QuestData;
import joshie.harvest.player.relationships.RelationshipData;
import joshie.harvest.player.stats.StatData;
import joshie.harvest.player.town.TownData;
import joshie.harvest.player.tracking.TrackingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class PlayerTracker {
    protected EntityNPCBuilder builder;
    
    public abstract EntityPlayer getAndCreatePlayer();
    public abstract UUID getUUID();
    public abstract FridgeData getFridge();   
    public abstract QuestData getQuests();
    public abstract RelationshipData getRelationships();
    public abstract StatData getStats();
    public abstract TownData getTown();
    public abstract TrackingData getTracking();
    public abstract EntityNPCBuilder getBuilder(World world);
}
