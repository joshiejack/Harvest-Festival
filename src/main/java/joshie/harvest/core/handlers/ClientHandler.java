package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerClient;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.crops.CropTrackerClient;
import joshie.harvest.npc.town.TownTrackerClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler extends SideHandler {
    private AnimalTrackerClient animals;
    private CalendarClient calendar;
    private CropTrackerClient crops;
    private TownTrackerClient town;
    
    public ClientHandler() {
        animals = new AnimalTrackerClient();
        calendar = new CalendarClient();
        crops = new CropTrackerClient();
        town = new TownTrackerClient();
    }

    @Override
    public AnimalTrackerClient getAnimalTracker() {
        return animals;
    }

    @Override
    public CalendarClient getCalendar() {
        return calendar;
    }

    @Override
    public CropTrackerClient getCropTracker() {
        return crops;
    }

    @Override
    public TownTrackerClient getTownTracker() {
        return town;
    }
}
