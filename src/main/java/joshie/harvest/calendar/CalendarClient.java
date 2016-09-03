package joshie.harvest.calendar;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CalendarClient extends Calendar {
    protected CalendarDate date = new CalendarDate(1, Season.SPRING, 1);

    @Override
    public CalendarDate getDate() {
        return date;
    }

    /* ############# Weather ################*/
    public void setForecast(Weather[] forecast) {
        this.forecast = forecast;
        updateWeatherStrength();
    }

    public void setStrength(float rain, float storm) {
        this.rainStrength = rain;
        this.stormStrength = storm;
    }
}
