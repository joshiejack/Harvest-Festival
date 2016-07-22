package joshie.harvest.core.helpers;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.calendar.HFCalendar;
import net.minecraft.world.World;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.calendar.HFCalendar.DAYS_PER_SEASON;
import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

public class CalendarHelper {
    private static final TIntObjectMap<Season> dimensionSeasons = new TIntObjectHashMap<>();
    private static final TIntObjectMap<Season> seasons = new TIntObjectHashMap<>();
    private static final Weekday[] DAYS;
    static {
        seasons.put(0, SPRING);
        seasons.put(1, SUMMER);
        seasons.put(2, AUTUMN);
        seasons.put(3, WINTER);

        //Add End and Nether Seasons as permenant
        dimensionSeasons.put(-1, NETHER);
        dimensionSeasons.put(1, END);
        DAYS = Weekday.class.getEnumConstants();
    }

    public static Weekday getWeekday(int days) {
        int modulus = days % 7;
        if (modulus < 0) modulus = 0;
        return DAYS[modulus];
    }

    public static Weekday getWeekday(long time) {
        return getWeekday(getElapsedDays(time));
    }

    public static void setDate(World world, ICalendarDate date) {
        long time = world.getWorldTime();
        date.setWeekday(getWeekday(time)).setDay(getDay(world, time)).setSeason(getSeason(world, time)).setYear(getYear(time));
    }

    public static int getYear(long totalTime) {
        return (int) Math.floor(getElapsedDays(totalTime) / 4 / DAYS_PER_SEASON);
    }

    public static Season getSeason(World world, long totalTime) {
        Season season = dimensionSeasons.get(world.provider.getDimension());
        return season == null ? seasons.get(Math.max(0, (int)Math.floor((getElapsedDays(totalTime) / DAYS_PER_SEASON) % 4))) : season;
    }

    public static int getDay(World world, long totalTime) {
        return dimensionSeasons.containsKey(world.provider.getDimension()) ? getElapsedDays(totalTime) : getElapsedDays(totalTime) % DAYS_PER_SEASON;
    }

    public static int getElapsedDays(long totalTime) {
        return (int) (totalTime / TICKS_PER_DAY);
    }

    public static int getTotalDays(int day, Season season, int year) {
        int current_days = day;
        int season_days = DAYS_PER_SEASON * season.ordinal();
        int year_days = (year - 1) * (DAYS_PER_SEASON * 4);
        return current_days + season_days + year_days;
    }

    public static int getTotalDays(ICalendarDate date) {
        int current_days = date.getDay();
        int season_days = HFCalendar.DAYS_PER_SEASON * date.getSeason().ordinal();
        int year_days = (date.getYear() - 1) * (DAYS_PER_SEASON * 4);
        return current_days + season_days + year_days;
    }

    public static int getYearsPassed(ICalendarDate birthday, ICalendarDate date) {
        int current_total_days = getTotalDays(date);
        int birthday_total_days = getTotalDays(birthday);
        int one_year = DAYS_PER_SEASON * 4;
        
        int years_passed = current_total_days / one_year; 
        int birthday_years = birthday_total_days / one_year;
                
        return years_passed - birthday_years;
    }

    public static long getTime(int day, Season season, int year) {
        return (getTotalDays(day, season, year)) * TICKS_PER_DAY;
    }

    public static long getTime(World world) {
        return (world.getWorldTime() + 6000) % TICKS_PER_DAY;
    }
    
    public static int getScaledTime(int time) {
        return (int) ((time / 24000D) * TICKS_PER_DAY);
    }
}
