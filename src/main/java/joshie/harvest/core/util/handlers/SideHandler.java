package joshie.harvest.core.util.handlers;

import joshie.harvest.animals.tracker.AnimalTracker;
import net.minecraft.world.World;

public abstract class SideHandler {
    public abstract void setWorld(World world);
    public abstract AnimalTracker getAnimalTracker();
}
