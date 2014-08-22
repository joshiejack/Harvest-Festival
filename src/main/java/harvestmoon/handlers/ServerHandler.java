package harvestmoon.handlers;

import harvestmoon.calendar.CalendarServer;
import harvestmoon.crops.CropTrackerServer;
import harvestmoon.entities.AnimalTrackerServer;
import harvestmoon.player.PlayerDataServer;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class ServerHandler {
    private static AnimalTrackerServer animals;
    private static CalendarServer calendar;
    private static CropTrackerServer crops;

    public ServerHandler(World world) {
        animals = (AnimalTrackerServer) world.loadItemData(AnimalTrackerServer.class, AnimalTrackerServer.DATA_NAME);
        if (animals == null) {
            animals = new AnimalTrackerServer(AnimalTrackerServer.DATA_NAME);
            world.setItemData(AnimalTrackerServer.DATA_NAME, (WorldSavedData) animals);
        }

        /** OLD SHIT **/
        calendar = (CalendarServer) world.loadItemData(CalendarServer.class, CalendarServer.DATA_NAME);
        if (calendar == null) {
            calendar = new CalendarServer(CalendarServer.DATA_NAME);
            world.setItemData(CalendarServer.DATA_NAME, (WorldSavedData) calendar);
        }

        crops = (CropTrackerServer) world.loadItemData(CropTrackerServer.class, CropTrackerServer.DATA_NAME);
        if (crops == null) {
            crops = new CropTrackerServer(CropTrackerServer.DATA_NAME);
            world.setItemData(CropTrackerServer.DATA_NAME, (WorldSavedData) crops);
        }
    }

    //Returns the serverside animal tracker
    public AnimalTrackerServer getAnimalTracker() {
        return animals;
    }
    
    //Returns the serverside calendar
    public CalendarServer getCalendar() {
        return calendar;
    }

    //Returns the serverside crop tracker
    public CropTrackerServer getCropTracker() {
        return crops;
    }

    //Returns the server side player data for this player
    public PlayerDataServer getPlayerData(EntityPlayer player) {
        return getPlayerData(player.worldObj, player.getPersistentID());
    }

  //Returns the server side player data for this UUID
    public PlayerDataServer getPlayerData(World world, UUID owner) {
        String check = PlayerDataServer.DATA_NAME + "-" + owner;
        PlayerDataServer data = (PlayerDataServer) world.loadItemData(PlayerDataServer.class, check);
        if (data == null) {
            data = new PlayerDataServer(check);
            world.setItemData(check, data);
        }

        return data;
    }

    public void removeAllRelations(EntityLivingBase entity) {
        for(EntityPlayer player: (List<EntityPlayer>)entity.worldObj.playerEntities) {
            getPlayerData(player).removeRelations(entity);
        }
    }
}
