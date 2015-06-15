package joshie.harvest.core.handlers;

import java.util.UUID;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.HFSavedData;
import joshie.harvest.crops.CropTrackerServer;
import joshie.harvest.mining.MineTrackerServer;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class ServerHandler {
    private HFSavedData data;

    public ServerHandler(World world) {
        data = (HFSavedData) world.loadItemData(HFSavedData.class, HFSavedData.DATA_NAME);
        if (data == null) {
            data = new HFSavedData(HFSavedData.DATA_NAME);
            world.setItemData(HFSavedData.DATA_NAME, data);
        }
    }
    
    public HFSavedData getData() {
        return data;
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
    public PlayerTrackerServer getPlayerData(EntityPlayer player) {
        return data.getPlayerData((EntityPlayerMP) player);
    }

    /** CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND **/
    public PlayerTrackerServer getPlayerData(UUID uuid) {
        return data.getPlayerData(uuid);
    }
}
