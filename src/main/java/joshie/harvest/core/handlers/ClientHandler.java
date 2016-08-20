package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerClient;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.town.TownTrackerClient;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler extends SideHandler {
    private AnimalTrackerClient animals = new AnimalTrackerClient();
    private CalendarClient calendar = new CalendarClient();
    private TownTrackerClient town = new TownTrackerClient();

    public ClientHandler() {}

    @Override
    public void setWorld(World world) {
        animals.setWorld(world);
        calendar.setWorld(world);
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
    public TownTrackerClient getTownTracker() {
        return town;
    }

    public boolean getSex() {
        return true;
    }
}
