package joshie.harvest.player;

import static joshie.harvest.core.helpers.ServerHelper.markDirty;
import static joshie.harvest.core.network.PacketHandler.sendToClient;

import java.util.UUID;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.calendar.CalendarDate;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.core.network.PacketSyncBirthday;
import joshie.harvest.core.network.PacketSyncFridge;
import joshie.harvest.core.network.PacketSyncGold;
import joshie.harvest.core.network.PacketSyncStats;
import joshie.harvest.core.util.IData;
import joshie.harvest.core.util.SellStack;
import joshie.harvest.init.HFNPCs;
import joshie.harvest.npc.EntityNPC;
import joshie.harvest.npc.EntityNPCBuilder;
import joshie.harvest.npc.NPC;
import joshie.harvest.player.Town.TownBuilding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class PlayerDataServer implements IData {
    private QuestStats questStats;
    private RelationStats relationStats;
    private ShippingStats shippingStats;
    private PlayerStats playerStats;
    private TrackingStats trackingStats;
    private FriendTracker friends;
    private FridgeContents fridge;
    private EntityNPCBuilder builder;
    private Town town;

    //References to the player and uuid this refers to
    private EntityPlayerMP player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER

    public PlayerDataServer() {
        questStats = new QuestStats(this);
        relationStats = new RelationStats(this);
        shippingStats = new ShippingStats(this);
        playerStats = new PlayerStats(this);
        trackingStats = new TrackingStats(this);
        friends = new FriendTracker(this);
        fridge = new FridgeContents(DimensionManager.getWorld(0));
        town = new Town(this);
    }

    public PlayerDataServer(EntityPlayerMP player) {
        this.player = player;
        this.uuid = UUIDHelper.getPlayerUUID(player);
        questStats = new QuestStats(this);
        relationStats = new RelationStats(this);
        shippingStats = new ShippingStats(this);
        playerStats = new PlayerStats(this);
        trackingStats = new TrackingStats(this);
        friends = new FriendTracker(this);
        fridge = new FridgeContents(player.worldObj);
        town = new Town(this);
    }

    //Pass the world that this player is currently in
    public EntityPlayerMP getAndCreatePlayer() {
        if (player == null) {
            player = EntityHelper.getPlayerFromUUID(uuid);
        }

        return player;
    }
    
    public UUID getUUID() {
        return uuid;
    }

    //The world is the world that this player is currently in
    public void newDay() {
        long gold = shippingStats.newDay();
        playerStats.addGold(gold);
        sendToClient(new PacketSyncGold(playerStats.getGold()), getAndCreatePlayer());
        relationStats.newDay();
        markDirty();
    }

    public void setTalkedTo(EntityLivingBase living) {
        relationStats.setTalkedTo(living);
    }

    //Sets this player as talked to
    public void setTalkedTo(INPC npc) {
        relationStats.setTalkedTo(npc);
    }

    //Sets this player as gifted
    public void setGifted(INPC npc, int value) {
        relationStats.setGifted(npc, value);
    }

    //Affecting Entity Relations
    public boolean affectRelationship(EntityLivingBase living, int amount) {
        return relationStats.affectRelationship(living, amount);
    }

    //Affecting NPC Relations
    public boolean affectRelationship(INPC npc, int amount) {
        return relationStats.affectRelationship(npc, amount);
    }

    //Get Relations for Entities
    public int getRelationship(EntityLivingBase living) {
        return relationStats.getRelationship(living);
    }

    //Get Relations for NPCs
    public int getRelationship(NPC npc) {
        return relationStats.getRelationship(npc);
    }

    //Remove Relations for Entities
    public boolean removeRelations(EntityLivingBase living) {
        return relationStats.removeRelations(living);
    }

    //Remove Relations for NPCs
    public boolean removeRelations(NPC npc) {
        return relationStats.removeRelations(npc);
    }

    public boolean isOnlineOrFriendsAre() {
        return friends.getFriends().size() > 0;
    }

    public boolean addForShipping(ItemStack stack) {
        boolean ret = shippingStats.addForShipping(stack);
        markDirty();
        return ret;
    }

    public FridgeContents getFridge() {
        return fridge;
    }

    public CalendarDate getBirthday() {
        return playerStats.getBirthday();
    }

    public void setBirthday() {
        if (playerStats.setBirthday()) {
            markDirty();
        }
    }

    public boolean setMarried(EntityNPC npc) {
        return relationStats.setMarried(npc);
    }

    public boolean canMarry() {
        return relationStats.canMarry();
    }

    public double getStamina() {
        return playerStats.getStamina();
    }

    public double getFatigue() {
        return playerStats.getFatigue();
    }

    public void affectStats(double stamina, double fatigue) {
        playerStats.affectStats(stamina, fatigue);
        markDirty();
    }

    public void syncPlayerStats() {
        sendToClient(new PacketSyncBirthday(playerStats.getBirthday()), getAndCreatePlayer());
        sendToClient(new PacketSyncGold(playerStats.getGold()), getAndCreatePlayer());
        sendToClient(new PacketSyncStats(playerStats.getStamina(), playerStats.getFatigue(), playerStats.getStaminaMax(), playerStats.getFatigueMin()), getAndCreatePlayer());
        sendToClient(new PacketSyncFridge(fridge), (EntityPlayerMP) getAndCreatePlayer());
        relationStats.sync();
    }

    public void addGold(long gold) {
        playerStats.addGold(gold);
        markDirty();
    }

    public void setGold(long gold) {
        playerStats.setGold(gold);
        markDirty();
    }

    public long getGold() {
        return playerStats.getGold();
    }

    public void addSold(SellStack stack) {
        trackingStats.addSold(stack);
        markDirty();
    }

    public void onHarvested(ICropData data) {
        trackingStats.onHarvested(data);
        markDirty();
    }

    public QuestStats getQuests() {
        return questStats;
    }

    public void addBuilding(World world, BuildingStage building) {
        town.addBuilding(world, building);
        markDirty();
    }

    public WorldLocation getCoordinatesFor(IBuilding home, String npc_location) {
        TownBuilding building = town.buildings.get(home.getName());
        if (building == null) return null;
        return building.getRealCoordinatesFor(npc_location);
    }
    

    public boolean hasBuilding(IBuilding building) {
        return town.buildings.get(building.getName()) != null;
    }
    
    //Cached Value, The actual data for the owner is stored in the entity itself
    public EntityNPCBuilder getBuilder(World world) {
        if (builder != null) return builder;
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = (Entity) world.loadedEntityList.get(i);
            if (entity instanceof EntityNPCBuilder) {
                UUID owner = ((EntityNPCBuilder)entity).owning_player;
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
    
    public TrackingStats getTrackingStats() {
        return trackingStats;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        uuid = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));

        //Read in the Basic Data Stuffs
        playerStats.readFromNBT(nbt);
        questStats.readFromNBT(nbt);
        relationStats.readFromNBT(nbt);
        shippingStats.readFromNBT(nbt);
        trackingStats.readFromNBT(nbt);
        friends.readFromNBT(nbt);
        fridge.readFromNBT(nbt);
        town.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("UUIDMost", uuid.getMostSignificantBits());
        nbt.setLong("UUIDLeast", uuid.getLeastSignificantBits());

        //Write the basic data to disk
        playerStats.writeToNBT(nbt);
        relationStats.writeToNBT(nbt);
        questStats.writeToNBT(nbt);
        shippingStats.writeToNBT(nbt);
        trackingStats.writeToNBT(nbt);
        friends.writeToNBT(nbt);
        fridge.writeToNBT(nbt);
        town.writeToNBT(nbt);
    }
}
