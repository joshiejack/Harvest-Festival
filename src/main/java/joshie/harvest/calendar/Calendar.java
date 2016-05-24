package joshie.harvest.calendar;

import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.HFTracker;
import net.minecraft.world.World;

public abstract class Calendar extends HFTracker {
    protected Weather[] forecast = new Weather[7];
    protected float rainStrength;
    private float stormStrength;

    public abstract ICalendarDate getDate();

    public ISeasonData getSeasonData() {
        return getDate().getSeasonData();
    }

    public void newDay() {}

    public void updateForecast() {}

    public Weather getForecast(int day) {
        day = Math.max(0, Math.min(6, day));
        return forecast[day];
    }

    public Weather getTodaysWeather() {
        return forecast[0] != null ? forecast[0] : Weather.SUNNY;
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

    public void recalculate(World world) {}
}