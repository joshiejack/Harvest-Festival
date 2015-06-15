package joshie.harvest.calendar;

import joshie.harvest.api.core.ICalendar;
import joshie.harvest.api.core.ICalendarDate;
import joshie.harvest.api.core.Season;
import joshie.harvest.core.handlers.DataHelper;

public class CalendarHelper implements ICalendar {
    @Override
    public ICalendarDate getToday() {
        return DataHelper.getCalendar().getDate();
    }

    @Override
    public ICalendarDate cloneDate(ICalendarDate date) {
        return new CalendarDate(date);
    }

    @Override
    public ICalendarDate newDate(int day, Season season, int year) {
        return new CalendarDate(day, season, year);
    }
}
