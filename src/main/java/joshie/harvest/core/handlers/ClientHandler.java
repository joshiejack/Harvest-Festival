package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerClient;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.crops.CropTrackerClient;
import joshie.harvest.player.PlayerTrackerClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler {
    private static AnimalTrackerClient animals;
    private static CalendarClient calendar;
    private static CropTrackerClient crops;
    private static PlayerTrackerClient player;
    
    public ClientHandler() {
        animals = new AnimalTrackerClient();
        calendar = new CalendarClient();
        crops = new CropTrackerClient();
        player = new PlayerTrackerClient();
    }

    public CalendarClient getCalendar() {
        return calendar;
    }

    public PlayerTrackerClient getPlayerData() {
        return player;
    }

    public AnimalTrackerClient getAnimalTracker() {
        return animals;
    }

    public CropTrackerClient getCropTracker() {
        return crops;
    }
}
