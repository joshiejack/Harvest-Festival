package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.town.TownTracker;
import net.minecraft.world.World;

public abstract class SideHandler {
    public abstract void setWorld(World world);
    public abstract AnimalTracker getAnimalTracker();
    public abstract TownTracker getTownTracker();
}
