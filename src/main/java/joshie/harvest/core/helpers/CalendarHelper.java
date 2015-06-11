package joshie.harvest.core.helpers;

import joshie.harvest.api.core.Season;
import joshie.harvest.api.core.Weekday;
import joshie.harvest.calendar.CalendarDate;
import joshie.harvest.core.config.Calendar;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CalendarHelper {
    @SideOnly(Side.CLIENT)
    public static int getClientDay() {
        return ClientHelper.getCalendar().getDate().getDay();
    }
    
    @SideOnly(Side.CLIENT)
    public static Season getClientSeason() {
        return ClientHelper.getCalendar().getDate().getSeason();
    }
    
    @SideOnly(Side.CLIENT)
    public static int getClientYear() {
        return ClientHelper.getCalendar().getDate().getYear();
    }
    
    @SideOnly(Side.CLIENT)
    public static CalendarDate getClientDate() {
        return ClientHelper.getCalendar().getDate();
    }
    
    public static CalendarDate getServerDate() {
        return ServerHelper.getCalendar().getDate();
    }
    
    public static CalendarDate getData(World world) {
        return world.isRemote? getClientDate(): getServerDate();
    }

    public static Weekday getWeekday(World world) {
        return world.isRemote? getClientDate().getWeekday(): getServerDate().getWeekday();
    }
    
    public static void setDate(int day, Season season, int year) {
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            ClientHelper.getCalendar().getDate().setDay(day).setSeason(season).setYear(year);
        } else {
            ServerHelper.getCalendar().setDate(day, season, year);
        }
    }
    
    public static Season getSeason() {
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            return ClientHelper.getCalendar().getDate().getSeason();
        } else return ServerHelper.getCalendar().getDate().getSeason();
    }
    
    public static int getTotalDays(CalendarDate date) {
        int current_days = date.getDay();
        int season_days = Calendar.DAYS_PER_SEASON * date.getSeason().ordinal();
        int year_days = (date.getYear() - 1) * (Calendar.DAYS_PER_SEASON * 4);
        return current_days + season_days + year_days;
    }

    public static int getYearsPassed(CalendarDate birthday, CalendarDate date) {
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
