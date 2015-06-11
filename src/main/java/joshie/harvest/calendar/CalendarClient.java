package joshie.harvest.calendar;

import joshie.harvest.api.core.Season;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CalendarClient {
    private CalendarDate date = new CalendarDate(1, Season.SPRING, 1);

    public CalendarDate getDate() {
        return date;
    }
}
