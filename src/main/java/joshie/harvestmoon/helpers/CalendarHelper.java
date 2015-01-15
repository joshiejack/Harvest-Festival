package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import joshie.harvestmoon.calendar.Season;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CalendarHelper {
    @SideOnly(Side.CLIENT)
    public static int getClientDay() {
        return handler.getClient().getCalendar().getDate().getDay();
    }
    
    @SideOnly(Side.CLIENT)
    public static Season getClientSeason() {
        return handler.getClient().getCalendar().getDate().getSeason();
    }
    
    @SideOnly(Side.CLIENT)
    public static int getClientYear() {
        return handler.getClient().getCalendar().getDate().getYear();
    }
    
    public static void setDate(int day, Season season, int year) {
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            handler.getClient().getCalendar().getDate().setDay(day).setSeason(season).setYear(year);
        } else {
            handler.getServer().getCalendar().setDate(day, season, year);
        }
    }
    
    public static Season getSeason() {
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            return handler.getClient().getCalendar().getDate().getSeason();
        } else return handler.getServer().getCalendar().getDate().getSeason();
    }
}
