package joshie.harvest.calendar.data;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.data.Calendar;
import joshie.harvest.calendar.provider.HFWorldProvider;
import joshie.harvest.calendar.render.CalendarRender;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CalendarClient extends Calendar {
    protected final CalendarDate date = new CalendarDate(1, Season.SPRING, 1);

    @Override
    public CalendarDate getDate() {
        return date;
    }

    @Override
    public void onSeasonChanged() {
        super.onSeasonChanged();
        CalendarRender.grassToBlend.clear();
        CalendarRender.leavesToBlend.clear();
        HFWorldProvider.reset();
    }

    /* ############# Weather ################*/
    public Weather getTomorrowsWeather() {
        return forecast[1];
    }

    public void setForecast(Weather[] forecast) {
        this.forecast = forecast;
        updateWeatherStrength();
    }

    public void setStrength(float rain, float storm) {
        this.rainStrength = rain;
        this.stormStrength = storm;
    }
}
