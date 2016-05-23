package joshie.harvest.api.calendar;

import joshie.harvest.api.core.ISeasonData;
import net.minecraft.world.World;


public interface ICalendar {
    ICalendarDate getToday(World world);
    ICalendarDate cloneDate(ICalendarDate date);
    ICalendarDate newDate(int day, Season season, int year);
    ISeasonData getDataForSeason(Season season);
}