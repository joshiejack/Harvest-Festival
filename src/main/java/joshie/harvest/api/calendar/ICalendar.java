package joshie.harvest.api.calendar;


public interface ICalendar {
    public ICalendarDate getToday();
    public ICalendarDate cloneDate(ICalendarDate date);
    public ICalendarDate newDate(int day, Season season, int year);
}
