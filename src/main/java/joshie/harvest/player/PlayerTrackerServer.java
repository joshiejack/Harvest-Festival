package joshie.harvest.player;

import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.player.quests.QuestDataServer;
import joshie.harvest.player.relationships.RelationshipDataServer;
import joshie.harvest.player.stats.StatsServer;
import joshie.harvest.player.tracking.TrackingServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class PlayerTrackerServer extends PlayerTracker {
    private QuestDataServer quests;
    private RelationshipDataServer relationships;
    private StatsServer stats;
    protected TrackingServer tracking;

    //References to the player and uuid this refers to
    private EntityPlayerMP player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER

    public PlayerTrackerServer(EntityPlayerMP player) {
        this.player = player;
        uuid = UUIDHelper.getPlayerUUID(player);
        quests = new QuestDataServer(this);
        relationships = new RelationshipDataServer();
        stats = new StatsServer();
        tracking = new TrackingServer(this);
    }

    //Pass the world that this player is currently in
    @Override
    public EntityPlayerMP getAndCreatePlayer() {
        if (player == null) {
            player = EntityHelper.getPlayerFromUUID(uuid);
        }

        return player;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public RelationshipDataServer getRelationships() {
        return relationships;
    }

    @Override
    public QuestDataServer getQuests() {
        return quests;
    }

    @Override
    public StatsServer getStats() {
        return stats;
    }

    public TrackingServer getTracking() {
        return tracking;
    }

    public void newDay() {
        //Add their gold from selling items
        relationships.newDay();
        EntityPlayerMP player = getAndCreatePlayer();
        if (player != null) {
            long gold = tracking.newDay();
            stats.addGold(null, gold);
            syncPlayerStats(player); //Resync everything
        }
    }

    public void syncPlayerStats(EntityPlayerMP player) {
        quests.sync(player);
        relationships.sync(player);
        stats.sync(player);
        tracking.sync(player);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        quests.readFromNBT(nbt.getCompoundTag("Quests"));
        relationships.readFromNBT(nbt.getCompoundTag("Relationships"));
        stats.readFromNBT(nbt.getCompoundTag("Stats"));
        tracking.readFromNBT(nbt.getCompoundTag("Tracking"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Quests", quests.writeToNBT(new NBTTagCompound()));
        nbt.setTag("Relationships", relationships.writeToNBT(new NBTTagCompound()));
        nbt.setTag("Stats", stats.writeToNBT(new NBTTagCompound()));
        nbt.setTag("Tracking", tracking.writeToNBT(new NBTTagCompound()));
        return nbt;
    }
}
