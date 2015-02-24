package joshie.harvestmoon.core.handlers;

import joshie.harvestmoon.animals.AnimalTrackerClient;
import joshie.harvestmoon.calendar.CalendarClient;
import joshie.harvestmoon.crops.CropTrackerClient;
import joshie.harvestmoon.player.PlayerDataClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler {
    private static AnimalTrackerClient animals;
    private static CalendarClient calendar;
    private static CropTrackerClient crops;
    private static PlayerDataClient player;
    
    public ClientHandler() {
        animals = new AnimalTrackerClient();
        calendar = new CalendarClient();
        crops = new CropTrackerClient();
        player = new PlayerDataClient();
    }

    public CalendarClient getCalendar() {
        return calendar;
    }

    public PlayerDataClient getPlayerData() {
        return player;
    }

    public AnimalTrackerClient getAnimalTracker() {
        return animals;
    }

    public CropTrackerClient getCropTracker() {
        return crops;
    }
}
