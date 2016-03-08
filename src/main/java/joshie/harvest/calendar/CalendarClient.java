package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Weather;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CalendarClient extends Calendar {
    @Override
    public void setForecast(Weather[] forecast) {
        this.forecast = forecast;
        updateWeatherStrength();
    }
}
