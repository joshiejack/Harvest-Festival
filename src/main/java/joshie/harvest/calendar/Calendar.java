package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ICalendarDate;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.api.core.Season;

public class Calendar {
    protected ICalendarDate date = HFApi.CALENDAR.newDate(1, Season.SPRING, 1);

    public ICalendarDate getDate() {
        return date;
    }

    public ISeasonData getSeasonData() {
        return SeasonData.getData(date.getSeason());
    }
}
