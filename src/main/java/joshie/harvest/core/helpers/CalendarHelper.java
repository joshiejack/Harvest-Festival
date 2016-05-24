package joshie.harvest.core.helpers;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.config.Calendar;
import net.minecraft.world.World;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.config.Calendar.DAYS_PER_SEASON;
import static joshie.harvest.core.config.Calendar.TICKS_PER_DAY;

public class CalendarHelper {
    private static final TIntObjectMap<Season> seasons = new TIntObjectHashMap<>();
    static {
        seasons.put(0, SPRING);
        seasons.put(1, SUMMER);
        seasons.put(2, AUTUMN);
        seasons.put(3, WINTER);
    }

    public static ICalendarDate getDate(World world) {
        long time = world.getWorldTime();
        return HFApi.calendar.newDate(getDay(time), getSeason(time), getYear(time));
    }

    public static void setDate(World world, ICalendarDate date) {
        long time = world.getWorldTime();
        date.setDay(getDay(time)).setSeason(getSeason(time)).setYear(getYear(time));
    }

    public static int getYear(long totalTime) {
        return (int) Math.floor(getElapsedDays(totalTime) / 4 / DAYS_PER_SEASON);
    }

    public static Season getSeason(long totalTime) {
        Season season = seasons.get(Math.max(0, (int)Math.floor((getElapsedDays(totalTime) / DAYS_PER_SEASON) % 4)));
        return season == null ? WINTER : season;
    }

    public static int getDay(long totalTime) {
        return getElapsedDays(totalTime) % DAYS_PER_SEASON;
    }

    public static int getElapsedDays(long totalTime) {
        return (int) (totalTime / TICKS_PER_DAY);
    }

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

    public static long getTime(World world) {
        return (world.getWorldTime() + 6000) % TICKS_PER_DAY;
    }
    
    public static int getScaledTime(int time) {
        return (int) ((time / 24000D) * TICKS_PER_DAY);
    }
}
