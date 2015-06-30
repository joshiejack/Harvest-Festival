package joshie.harvest.calendar;

import static joshie.harvest.core.util.generic.Text.BRIGHT_GREEN;
import static joshie.harvest.core.util.generic.Text.INDIGO;
import static joshie.harvest.core.util.generic.Text.ORANGE;
import static joshie.harvest.core.util.generic.Text.YELLOW;

import java.util.EnumMap;

import joshie.harvest.api.calendar.ICalendar;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.handlers.HFTrackers;

public class CalendarHelper implements ICalendar {
    @Override
    public ICalendarDate getToday() {
        return HFTrackers.getCalendar().getDate();
    }

    @Override
    public ICalendarDate cloneDate(ICalendarDate date) {
        return new CalendarDate(date);
    }

    @Override
    public ICalendarDate newDate(int day, Season season, int year) {
        return new CalendarDate(day, season, year);
    }
    
    private static final EnumMap<Season, SeasonData> data = new EnumMap(Season.class);
    static {
        data.put(Season.SPRING, new SeasonData(Season.SPRING, 0x87CEFA, 0.6082D, 0.01F, 1850L, BRIGHT_GREEN, 0x00D900, 80D, 100D, 0D, 0D, 0D));
        data.put(Season.SUMMER, new SeasonData(Season.SUMMER, 7972863, 0.1D, 0.0011F, 0L, YELLOW, 0xFFFF4D, 95D, 75D, 100D, 0D, 0D));
        data.put(Season.AUTUMN, new SeasonData(Season.AUTUMN, 0x8CBED6, 1.0D, -0.0325F, 2400L, ORANGE, 0x8C4600, 50D, 100D, 0D, 0D, 0D));
        data.put(Season.WINTER, new SeasonData(Season.WINTER, 0xFFFFFF, 1.35D, -0.09F, 2950L, INDIGO, 0xFFFFFF, 45D, 0D, 0D, 90D, 100D));
    }
    
    @Override
    public ISeasonData getDataForSeason(Season season) {
        return data.get(season);
    }
}
