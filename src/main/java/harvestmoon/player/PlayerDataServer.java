package harvestmoon.player;

import static harvestmoon.network.PacketHandler.sendAround;
import static harvestmoon.network.PacketHandler.sendToClient;
import harvestmoon.cooking.SavedRecipe;
import harvestmoon.crops.CropData;
import harvestmoon.network.PacketSyncGold;
import harvestmoon.network.PacketSyncRelations;
import harvestmoon.network.PacketSyncStats;
import harvestmoon.util.SellStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

public class PlayerDataServer extends WorldSavedData {
    public static final String DATA_NAME = "HM-Tracker-Players";

    private HashMap<UUID, Short> relationships = new HashMap();
    private ArrayList<SavedRecipe> savedRecipes = new ArrayList();
    private ShippingStats shippingStats = new ShippingStats();
    private PlayerStats playerStats = new PlayerStats();
    private TrackingStats trackingStats = new TrackingStats();

    public PlayerDataServer(String string) {
        super(string);
    }

    public void newDay(EntityPlayerMP player) {
        int gold = shippingStats.newDay(player);
        playerStats.addGold(gold);
        playerStats.newDay();
        sendToClient(new PacketSyncGold(playerStats.getGold()), player);
        sendToClient(new PacketSyncStats(playerStats.getStamina(), playerStats.getFatigue()), player);
        markDirty();
    }
    
    public void affectRelationship(EntityLivingBase entity, int amount) {
        int relations = getRelationship(entity) + amount;
        relationships.put(entity.getPersistentID(), (short) relations);
        markDirty();
        sendAround(new PacketSyncRelations(entity.getEntityId(), relations, true), entity.worldObj.provider.dimensionId, entity.posX, entity.posY, entity.posZ);
    }

    public int getRelationship(EntityLivingBase entity) {
        Short ret = relationships.get(entity.getPersistentID());
        return ret == null ? 0 : ret;
    }

    public void removeRelations(EntityLivingBase entity) {
        relationships.remove(entity.getPersistentID());
        markDirty();
    }

    public boolean addForShipping(ItemStack stack) {
        boolean ret = shippingStats.addForShipping(stack);
        markDirty();
        return ret;
    }

    public void setBirthday() {
        if (playerStats.setBirthday()) {
            markDirty();
        }
    }

    public void affectStats(double stamina, double fatigue) {
        playerStats.affectStats(stamina, fatigue);
        markDirty();
    }

    public void syncPlayerStats(EntityPlayerMP player) {
        sendToClient(new PacketSyncGold(playerStats.getGold()), player);
        sendToClient(new PacketSyncStats(playerStats.getStamina(), playerStats.getFatigue()), player);
    }

    public void addSold(SellStack stack) {
        trackingStats.addSold(stack);
        markDirty();
    }

    public void onHarvested(CropData data) {
        trackingStats.onHarvested(data);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        //Read in the Relationships
        NBTTagList relations = nbt.getTagList("Relationships", 10);
        for (int i = 0; i < relations.tagCount(); i++) {
            NBTTagCompound tag = relations.getCompoundTagAt(i);
            UUID id = new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));
            short value = tag.getShort("Value");
            relationships.put(id, value);
        }

        /////////////////////////////////////////////////////////////////////////////

        //Read in the SavedRecipes
        NBTTagList recipes = nbt.getTagList("SavedRecipes", 10);
        for (int i = 0; i < recipes.tagCount(); i++) {
            NBTTagCompound tag = recipes.getCompoundTagAt(i);
            SavedRecipe recipe = new SavedRecipe();
            recipe.readFromNBT(tag);
            savedRecipes.add(recipe);
        }

        //Read in the Basic Data Stuffs
        shippingStats.readFromNBT(nbt);
        playerStats.readFromNBT(nbt);
        trackingStats.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        //Saving the Relationships
        NBTTagList relations = new NBTTagList();
        for (Map.Entry<UUID, Short> entry : relationships.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong("UUIDMost", entry.getKey().getMostSignificantBits());
            tag.setLong("UUIDLeast", entry.getKey().getLeastSignificantBits());
            tag.setShort("Value", entry.getValue());
            relations.appendTag(tag);
        }

        nbt.setTag("Relationships", relations);

        ///////////////////////////////////////////////////////////////////////////

        //Saving the SavedRecipes
        NBTTagList recipes = new NBTTagList();
        for (SavedRecipe recipe : savedRecipes) {
            NBTTagCompound tag = new NBTTagCompound();
            recipe.writeToNBT(tag);
            recipes.appendTag(tag);
        }

        nbt.setTag("SavedRecipes", recipes);

        //Write the basic data to disk
        shippingStats.writeToNBT(nbt);
        playerStats.writeToNBT(nbt);
        trackingStats.writeToNBT(nbt);
    }
}
