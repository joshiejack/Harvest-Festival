package joshie.harvest.core.helpers;

import joshie.harvest.api.core.ICalendarDate;
import joshie.harvest.core.config.Calendar;
import net.minecraft.world.World;

public class CalendarHelper {        
    public static int getTotalDays(ICalendarDate date) {
        int current_days = date.getDay();
        int season_days = Calendar.DAYS_PER_SEASON * date.getSeason().ordinal();
        int year_days = (date.getYear() - 1) * (Calendar.DAYS_PER_SEASON * 4);
        return current_days + season_days + year_days;
    }

    public static int getYearsPassed(ICalendarDate birthday, ICalendarDate date) {
        int current_total_days = getTotalDays(date);
        int birthday_total_days = getTotalDays(birthday);
        int one_year = Calendar.DAYS_PER_SEASON * 4;
        
        int years_passed = current_total_days / one_year; 
        int birthday_years = birthday_total_days / one_year;
                
        return years_passed - birthday_years;
    }

    public static void newDay() {
        ServerHelper.getCalendar().newDay();
    }

    public static long getTime(World world) {
        return world.getWorldTime() % Calendar.TICKS_PER_DAY;
    }
    
    public static int getScaledTime(int time) {
        return (int) ((time / 24000D) * Calendar.TICKS_PER_DAY);
    }
}
