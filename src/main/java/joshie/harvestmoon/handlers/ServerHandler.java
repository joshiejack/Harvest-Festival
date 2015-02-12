package joshie.harvestmoon.handlers;

import java.util.List;

import joshie.harvestmoon.HMSavedData;
import joshie.harvestmoon.animals.AnimalTrackerServer;
import joshie.harvestmoon.calendar.CalendarServer;
import joshie.harvestmoon.crops.CropTrackerServer;
import joshie.harvestmoon.mining.MineTrackerServer;
import joshie.harvestmoon.player.PlayerDataServer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class ServerHandler {
    private HMSavedData data;

    public ServerHandler(World world) {
        data = (HMSavedData) world.loadItemData(HMSavedData.class, HMSavedData.DATA_NAME);
        if (data == null) {
            data = new HMSavedData(HMSavedData.DATA_NAME);
            world.setItemData(HMSavedData.DATA_NAME, data);
        }
    }

    //Returns the serverside animal tracker
    public AnimalTrackerServer getAnimalTracker() {
        return data.getAnimalTracker();
    }

    //Returns the serverside calendar
    public CalendarServer getCalendar() {
        return data.getCalendar();
    }

    //Returns the serverside crop tracker
    public CropTrackerServer getCropTracker() {
        return data.getCropTracker();
    }
    
    //Returns the serverside mines tracker
    public MineTrackerServer getMineTracker() {
        return data.getMineTracker();
    }

    //Marks the data as having changed
    public void markDirty() {
        data.markDirty();
    }

    //Returns the server side player data for this player
    public PlayerDataServer getPlayerData(EntityPlayer player) {
        return data.getPlayerData((EntityPlayerMP) player);
    }

    //Removes all relations
    public void removeAllRelations(EntityLivingBase entity) {
        for (EntityPlayer player : (List<EntityPlayer>) entity.worldObj.playerEntities) {
            getPlayerData(player).removeRelations(entity);
        }
    }
}
