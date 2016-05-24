package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerClient;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.crops.tracker.CropTrackerClient;
import joshie.harvest.npc.town.TownTrackerClient;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler extends SideHandler {
    private AnimalTrackerClient animals = new AnimalTrackerClient();
    private CalendarClient calendar = new CalendarClient();
    private CropTrackerClient crops = new CropTrackerClient();
    private TownTrackerClient town = new TownTrackerClient();

    public ClientHandler(World world) {
        animals.setWorld(world);
        calendar.setWorld(world);
        crops.setWorld(world);
        town.setWorld(world);
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
