package joshie.harvestmoon.helpers;

import joshie.harvestmoon.animals.AnimalTrackerClient;
import joshie.harvestmoon.calendar.CalendarClient;
import joshie.harvestmoon.crops.CropTrackerClient;
import joshie.harvestmoon.handlers.ClientHandler;
import joshie.harvestmoon.player.PlayerDataClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientHelper {
    @SideOnly(Side.CLIENT)
    private static ClientHandler theClient;

    //Creates a new instance of client handler
    @SideOnly(Side.CLIENT)
    public static void resetClient() {
        theClient = new ClientHandler();
    }

    public static AnimalTrackerClient getAnimalTracker() {
        return theClient.getAnimalTracker();
    }

    public static CalendarClient getCalendar() {
        return theClient.getCalendar();
    }

    public static CropTrackerClient getCropTracker() {
        return theClient.getCropTracker();
    }

    public static PlayerDataClient getPlayerData() {
        return theClient.getPlayerData();
    }
}
