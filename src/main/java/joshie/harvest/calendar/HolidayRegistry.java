package joshie.harvest.calendar;

import joshie.harvest.api.calendar.CalendarDate;

import java.util.HashSet;
import java.util.Set;

public class HolidayRegistry {
    public static final HolidayRegistry INSTANCE = new HolidayRegistry();
    private final Set<CalendarDate> holidays = new HashSet<>();

    public boolean isHoliday(CalendarDate date) {
        for (CalendarDate holiday : holidays) {
            if (holiday.isSameDay(date)) return true;
        }

        return false;
    }
}
