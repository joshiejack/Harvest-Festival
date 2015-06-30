package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;

public class Calendar {
    protected ICalendarDate date = HFApi.CALENDAR.newDate(1, Season.SPRING, 1);
    protected Weather[] forecast = new Weather[7];
    protected float rainStrength;
    protected float stormStrength;

    public ICalendarDate getDate() {
        return date;
    }

    public ISeasonData getSeasonData() {
        return date.getSeasonData();
    }

    public void newDay(long bedtime) {};
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

    public float getTodaysRainStrength() {
        return rainStrength;
    }
    
    public float getTodaysStormStrength() {
        return stormStrength;
    }
    
    public void updateWeatherStrength() {
        switch (forecast[0]) {
            case SUNNY:
                rainStrength = 0F;
                stormStrength = 0F;
                break;
            case RAIN:
            case SNOW:
                rainStrength = 1F;
                stormStrength = 0F;
                break;
            case TYPHOON:
                rainStrength = 2F;
                stormStrength = 1F;
                break;
            case BLIZZARD:
                rainStrength = 2F;
                stormStrength = 0F;
                break;
            default:
                break;
        }
    }
}
