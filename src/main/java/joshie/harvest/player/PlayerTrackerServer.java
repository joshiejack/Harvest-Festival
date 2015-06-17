package joshie.harvest.player;

import java.util.UUID;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.player.fridge.FridgeDataServer;
import joshie.harvest.player.quests.QuestDataServer;
import joshie.harvest.player.relationships.RelationshipDataServer;
import joshie.harvest.player.stats.StatDataServer;
import joshie.harvest.player.town.TownDataServer;
import joshie.harvest.player.tracking.TrackingDataServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PlayerTrackerServer extends PlayerTracker {
    private FridgeDataServer fridge;
    private QuestDataServer quests;
    private RelationshipDataServer relationships;
    private StatDataServer stats;
    private TownDataServer town;
    protected TrackingDataServer tracking;

    //References to the player and uuid this refers to
    private EntityPlayerMP player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER

    public PlayerTrackerServer() {
        fridge = new FridgeDataServer();
        quests = new QuestDataServer(this);
        relationships = new RelationshipDataServer();
        stats = new StatDataServer();
        town = new TownDataServer();
        tracking = new TrackingDataServer();
    }

    public PlayerTrackerServer(EntityPlayerMP player) {
        this.player = player;
        uuid = UUIDHelper.getPlayerUUID(player);
        fridge = new FridgeDataServer();
        quests = new QuestDataServer(this);
        relationships = new RelationshipDataServer();
        stats = new StatDataServer();
        town = new TownDataServer();
        tracking = new TrackingDataServer();
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
    public FridgeDataServer getFridge() {
        return fridge;
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
    public StatDataServer getStats() {
        return stats;
    }
    
    @Override
    public TownDataServer getTown() {
        return town;
    }
    
    public TrackingDataServer getTracking() {
        return tracking;
    }

    @Override
    public EntityNPCBuilder getBuilder(World world) {
        if (builder != null) return builder;
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = (Entity) world.loadedEntityList.get(i);
            if (entity instanceof EntityNPCBuilder) {
                UUID owner = ((EntityNPCBuilder) entity).owning_player;
                if (owner == uuid) {
                    builder = (EntityNPCBuilder) entity;
                    return builder;
                }
            }
        }

        //Create an npc builder, with this person as their owner
        EntityNPCBuilder builder = (EntityNPCBuilder) NPCHelper.getEntityForNPC(uuid, world, HFNPCs.builder);
        EntityPlayer player = getAndCreatePlayer();
        builder.setPosition(player.posX + player.worldObj.rand.nextDouble() * 4D, player.posY, player.posZ + player.worldObj.rand.nextDouble() * 4D);
        world.spawnEntityInWorld(builder);
        return builder;
    }

    public void newDay() {
        //Add their gold from selling items
        relationships.newDay();
        EntityPlayerMP player = getAndCreatePlayer();
        if (player != null) {
            stats.addGold(player, tracking.newDay());
            syncPlayerStats(player); //Resync everything
        }

        HFTrackers.markDirty();
    }

    public void syncPlayerStats(EntityPlayerMP player) {
        fridge.sync(player);
        quests.sync(player);
        relationships.sync(player);
        stats.sync(player);
        town.sync(player);
        tracking.sync(player);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        uuid = UUID.fromString(nbt.getString("UUID"));
        fridge.readFromNBT(nbt);
        quests.readFromNBT(nbt);
        relationships.readFromNBT(nbt);
        stats.readFromNBT(nbt);
        town.readFromNBT(nbt);
        tracking.readFromNBT(nbt);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("UUID", uuid.toString());
        fridge.writeToNBT(nbt);
        quests.writeToNBT(nbt);
        relationships.writeToNBT(nbt);
        stats.writeToNBT(nbt);
        town.writeToNBT(nbt);
        tracking.writeToNBT(nbt);
    }
}
