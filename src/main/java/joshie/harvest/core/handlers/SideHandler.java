package joshie.harvest.core.handlers;

import java.util.UUID;

import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.crops.CropTracker;
import joshie.harvest.mining.MineTracker;
import joshie.harvest.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;

public abstract class SideHandler {
    public abstract Calendar getCalendar();
    public abstract AnimalTracker getAnimalTracker();
    public abstract CropTracker getCropTracker();
    public abstract MineTracker getMineTracker();
    public abstract PlayerTracker getPlayerTracker(EntityPlayer player);
    public abstract PlayerTracker getPlayerTracker(UUID uuid);
}
