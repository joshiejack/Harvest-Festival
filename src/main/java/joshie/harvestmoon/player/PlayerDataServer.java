package joshie.harvestmoon.player;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.network.PacketHandler.sendToClient;

import java.util.ArrayList;
import java.util.UUID;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.cooking.SavedRecipe;
import joshie.harvestmoon.crops.CropData;
import joshie.harvestmoon.entities.npc.NPC;
import joshie.harvestmoon.helpers.generic.EntityHelper;
import joshie.harvestmoon.network.PacketSyncBirthday;
import joshie.harvestmoon.network.PacketSyncGold;
import joshie.harvestmoon.network.PacketSyncStats;
import joshie.harvestmoon.util.IData;
import joshie.harvestmoon.util.SellStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerDataServer implements IData {
    private ArrayList<SavedRecipe> savedRecipes = new ArrayList();
    private QuestStats questStats;
    private RelationStats relationStats;
    private ShippingStats shippingStats;
    private PlayerStats playerStats;
    private TrackingStats trackingStats;

    //References to the player and uuid this refers to
    private EntityPlayerMP player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER

    public PlayerDataServer() {
        questStats = new QuestStats(this);
        relationStats = new RelationStats(this);
        shippingStats = new ShippingStats(this);
        playerStats = new PlayerStats(this);
        trackingStats = new TrackingStats(this);
    }
    
    public PlayerDataServer(EntityPlayerMP player) {
        this.player = player;
        this.uuid = player.getPersistentID();
        questStats = new QuestStats(this);
        relationStats = new RelationStats(this);
        shippingStats = new ShippingStats(this);
        playerStats = new PlayerStats(this);
        trackingStats = new TrackingStats(this);
    }

    //Pass the world that this player is currently in
    public EntityPlayerMP getAndCreatePlayer() {
        if(player == null) {
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
        handler.getServer().markDirty();
    }

    public void setTalkedTo(EntityLivingBase living) {
        relationStats.setTalkedTo(living);
    }
    
    //Sets this player as talked to
    public void setTalkedTo(NPC npc) {
        relationStats.setTalkedTo(npc);
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

    public boolean addForShipping(ItemStack stack) {
        boolean ret = shippingStats.addForShipping(stack);
        handler.getServer().markDirty();
        return ret;
    }

    public CalendarDate getBirthday() {
        return playerStats.getBirthday();
    }

    public void setBirthday() {
        if (playerStats.setBirthday()) {
            handler.getServer().markDirty();
        }
    }

    public double getStamina() {
        return playerStats.getStamina();
    }

    public double getFatigue() {
        return playerStats.getFatigue();
    }

    public void affectStats(double stamina, double fatigue) {
        playerStats.affectStats(stamina, fatigue);
        handler.getServer().markDirty();
    }

    public void syncPlayerStats() {
        sendToClient(new PacketSyncBirthday(playerStats.getBirthday()), getAndCreatePlayer());
        sendToClient(new PacketSyncGold(playerStats.getGold()), getAndCreatePlayer());
        sendToClient(new PacketSyncStats(playerStats.getStamina(), playerStats.getFatigue(), playerStats.getStaminaMax(), playerStats.getFatigueMin()), getAndCreatePlayer());
    }

    public void addGold(long gold) {
        playerStats.addGold(gold);
        handler.getServer().markDirty();
    }
    
    public void setGold(long gold) {
        playerStats.setGold(gold);
        handler.getServer().markDirty();
    }

    public long getGold() {
        return playerStats.getGold();
    }

    public void addSold(SellStack stack) {
        trackingStats.addSold(stack);
        handler.getServer().markDirty();
    }

    public void onHarvested(CropData data) {
        trackingStats.onHarvested(data);
        handler.getServer().markDirty();
    }

    public QuestStats getQuests() {
        return questStats;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        uuid = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
        
        //Read in the SavedRecipes
        NBTTagList recipes = nbt.getTagList("SavedRecipes", 10);
        for (int i = 0; i < recipes.tagCount(); i++) {
            NBTTagCompound tag = recipes.getCompoundTagAt(i);
            SavedRecipe recipe = new SavedRecipe();
            recipe.readFromNBT(tag);
            savedRecipes.add(recipe);
        }

        //Read in the Basic Data Stuffs
        playerStats.readFromNBT(nbt);
        questStats.readFromNBT(nbt);
        relationStats.readFromNBT(nbt);
        shippingStats.readFromNBT(nbt);
        trackingStats.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("UUIDMost", uuid.getMostSignificantBits());
        nbt.setLong("UUIDLeast", uuid.getLeastSignificantBits());
        
        //Saving the SavedRecipes
        NBTTagList recipes = new NBTTagList();
        for (SavedRecipe recipe : savedRecipes) {
            NBTTagCompound tag = new NBTTagCompound();
            recipe.writeToNBT(tag);
            recipes.appendTag(tag);
        }

        nbt.setTag("SavedRecipes", recipes);

        //Write the basic data to disk
        playerStats.writeToNBT(nbt);
        relationStats.writeToNBT(nbt);
        questStats.writeToNBT(nbt);
        shippingStats.writeToNBT(nbt);
        trackingStats.writeToNBT(nbt);
    }
}
