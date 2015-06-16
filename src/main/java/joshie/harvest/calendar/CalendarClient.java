package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Weather;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CalendarClient extends Calendar {
    @Override
    public void setForecast(Weather[] forecast) {
        this.forecast = forecast;
    }
}
