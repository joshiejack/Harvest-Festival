package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.crops.CropTracker;
import joshie.harvest.npc.town.TownTracker;

public abstract class SideHandler {
    public abstract Calendar getCalendar();
    public abstract AnimalTracker getAnimalTracker();
    public abstract CropTracker getCropTracker();
    public abstract TownTracker getTownTracker();
}
