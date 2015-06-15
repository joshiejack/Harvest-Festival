package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ISeasonData;

public class Calendar {
    protected ICalendarDate date = HFApi.CALENDAR.newDate(1, Season.SPRING, 1);

    public ICalendarDate getDate() {
        return date;
    }

    public ISeasonData getSeasonData() {
        return date.getSeasonData();
    }

    public void newDay() {}
}
