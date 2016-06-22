package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.npc.town.TownTracker;
import net.minecraft.world.World;

public abstract class SideHandler {
    public abstract void setWorld(World world);
    public abstract Calendar getCalendar();
    public abstract AnimalTracker getAnimalTracker();
    public abstract TownTracker getTownTracker();
}
