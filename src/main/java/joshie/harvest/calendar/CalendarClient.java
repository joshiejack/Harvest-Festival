package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CalendarClient extends Calendar {
    protected ICalendarDate date = HFApi.calendar.newDate(1, Season.SPRING, 1);

    @Override
    public ICalendarDate getDate() {
        return date;
    }

    public void setForecast(Weather[] forecast) {
        this.forecast = forecast;
        updateWeatherStrength();
    }
}
