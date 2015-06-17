package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncForecast;

public class Calendar {
    protected ICalendarDate date = HFApi.CALENDAR.newDate(1, Season.SPRING, 1);
    protected Weather[] forecast = new Weather[7];

    public ICalendarDate getDate() {
        return date;
    }

    public ISeasonData getSeasonData() {
        return date.getSeasonData();
    }

    public void newDay() {};
    public void updateForecast() {};
    
    public Weather getForecast(int day) {
        day = Math.max(0, Math.min(6, day));
        return forecast[day];
    }

    public Weather getTodaysWeather() {
        return forecast[0] != null? forecast[0] : Weather.SUNNY;
    }

    public void setTodaysWeather(Weather weather) {}
    public void setForecast(Weather[] forecast) {}
}
