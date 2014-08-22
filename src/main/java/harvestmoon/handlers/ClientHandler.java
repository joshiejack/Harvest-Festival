package harvestmoon.handlers;

import harvestmoon.calendar.CalendarClient;
import harvestmoon.crops.CropTrackerClient;
import harvestmoon.entities.AnimalTrackerClient;
import harvestmoon.player.PlayerDataClient;

public class ClientHandler {
    private static AnimalTrackerClient animals = new AnimalTrackerClient();
    private static CalendarClient calendar = new CalendarClient();
    private static CropTrackerClient crops = new CropTrackerClient();
    private static PlayerDataClient player = new PlayerDataClient();

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
