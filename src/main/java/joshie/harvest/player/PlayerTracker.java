package joshie.harvest.player;

import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.quests.QuestStats;
import joshie.harvest.relations.RelationshipTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class PlayerTracker {
    protected FridgeContents fridge = new FridgeContents();
    protected FriendTracker friends = new FriendTracker(this);
    protected PlayerStats playerStats = new PlayerStats();
    protected ShippingStats shippingStats = new ShippingStats(this);
    protected TrackingStats trackingStats = new TrackingStats();
    protected Town town = new Town();

    public abstract EntityPlayer getAndCreatePlayer();
    
    public FridgeContents getFridge() {
        return fridge;
    }

    public void setFridge(FridgeContents fridge) {
        this.fridge = fridge;
    }

    public FriendTracker getFriendTracker() {
        return friends;
    }
    
    public PlayerStats getStats() {
        return playerStats;
    }

    public abstract QuestStats getQuests();
    
    public ShippingStats getShipping() {
        return shippingStats;
    }

    public TrackingStats getTracking() {
        return trackingStats;
    }

    public Town getTown() {
        return town;
    }
    
    public abstract RelationshipTracker getRelationships();
    public void syncPlayerStats() {}
    public abstract EntityNPCBuilder getBuilder(World world);
    public void newDay() {}
}
