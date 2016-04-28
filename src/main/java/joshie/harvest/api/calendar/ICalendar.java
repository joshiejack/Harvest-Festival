package joshie.harvest.api.calendar;

import joshie.harvest.api.core.ISeasonData;


public interface ICalendar {
    public ICalendarDate getToday();
    public ICalendarDate cloneDate(ICalendarDate date);
    public ICalendarDate newDate(int day, Season season, int year);
    public ISeasonData getDataForSeason(Season season);
}