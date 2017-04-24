package joshie.harvest.core.handlers;

import joshie.harvest.animals.tracker.AnimalTrackerServer;
import net.minecraft.world.World;

public class ServerHandler {
    private final AnimalTrackerServer animals = new AnimalTrackerServer();
    private final DailyTickHandler ticking = new DailyTickHandler();

    public ServerHandler(World world) {
        animals.setWorld(world);
        ticking.setWorld(world);
    }

    public AnimalTrackerServer getAnimalTracker() {
        return animals;
    }

    public DailyTickHandler getTickables() {
        return ticking;
    }
}
