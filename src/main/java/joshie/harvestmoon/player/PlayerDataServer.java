package joshie.harvestmoon.player;

import static joshie.harvestmoon.core.helpers.ServerHelper.markDirty;
import static joshie.harvestmoon.core.network.PacketHandler.sendToClient;

import java.util.UUID;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.buildings.BuildingStage;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.core.helpers.UUIDHelper;
import joshie.harvestmoon.core.helpers.generic.EntityHelper;
import joshie.harvestmoon.core.network.PacketSyncBirthday;
import joshie.harvestmoon.core.network.PacketSyncFridge;
import joshie.harvestmoon.core.network.PacketSyncGold;
import joshie.harvestmoon.core.network.PacketSyncStats;
import joshie.harvestmoon.core.util.IData;
import joshie.harvestmoon.core.util.SellStack;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.NPC;
import joshie.harvestmoon.player.Town.TownBuilding;
import net.minecraft.entity.EntityLivingBase;
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
    public void setTalkedTo(NPC npc) {
        relationStats.setTalkedTo(npc);
    }

    //Sets this player as gifted
    public void setGifted(NPC npc, int value) {
        relationStats.setGifted(npc, value);
    }

    //Affecting Entity Relations
    public boolean affectRelationship(EntityLivingBase living, int amount) {
        return relationStats.affectRelationship(living, amount);
    }

    //Affecting NPC Relations
    public boolean affectRelationship(NPC npc, int amount) {
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

    public WorldLocation getCoordinatesFor(BuildingGroup group, String npc_location) {
        TownBuilding building = town.buildings.get(group.getName());
        if (building == null) return null;
        return building.getRealCoordinatesFor(npc_location);
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
