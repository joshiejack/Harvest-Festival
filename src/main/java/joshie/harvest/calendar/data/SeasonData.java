package joshie.harvest.calendar.data;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;
import static joshie.harvest.core.lib.HFModInfo.MODID;

public class SeasonData {
    private final WeightedWeather weather = new WeightedWeather();
    private final ResourceLocation resource;
    public int skyColor, grassColor, leavesColor;
    private int sunrise;
    private int sunset;
    private int midday;
    private int midnight;

    public SeasonData(Season season, int skyColor, int sunrise, int sunset) {
        this.resource = new ResourceLocation(MODID, "textures/gui/" + season.name().toLowerCase(Locale.ENGLISH) + ".png");
        this.skyColor = skyColor;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.recalculateMiddayAndMidnight();
    }

    public SeasonData setLeavesColor(int color) {
        this.leavesColor = color;
        return this;
    }

    public SeasonData setGrassColor(int color) {
        this.grassColor = color;
        return this;
    }

    public SeasonData setWeatherWeight(Weather weather, double value) {
        this.weather.add(weather, value);
        return this;
    }

    public int getSunset() {
        return sunset;
    }

    private void recalculateMiddayAndMidnight() {
        midday = (int) ((double) (sunrise + sunset) / 2D);
        midnight = (int) ((double) (sunrise + sunset + 24000) / 2D);
    }

    public ResourceLocation getResource() {
        return resource;
    }

    private float convertRange(float oldMin, float oldMax, float newMin, float newMax, float value) {
        float range = (oldMax - oldMin);
        if (range == 0) return newMin;
        else {
            return (((value - oldMin) * ((newMax - newMin))) / range) + newMin;
        }
    }

    public float getCelestialAngle(long time) {
        time = TICKS_PER_DAY == 24000 ? time + 6000 : (long) convertRange(0, TICKS_PER_DAY - 1, 0, 23999, time) + 6000;
        if (time < sunrise) time += 24000;
        if (time >= sunrise && time < midday) return convertRange(sunrise, midday - 1, 0.757F, 1F, time);
        else if (time >= midday && time < sunset) return convertRange(midday, sunset - 1, 0F, 0.2425F, time);
        else if (time >= sunset && time < midnight) return convertRange(sunset, midnight - 1, 0.2425F, 0.5F, time);
        else if (time >= midnight) return convertRange(midnight, 24000 + sunrise, 0.5F, 0.757F, time);
        else return 1F;
    }

    public Weather getWeather(Random rand) {
        return weather.get(rand);
    }

    private class WeightedWeather {
        private final NavigableMap<Double, Weather> map = new TreeMap<>();
        private double total = 0;

        public void add(Weather state, double weight) {
            if (weight <= 0) return;
            total += weight;
            map.put(total, state);
        }

        public Weather get(Random rand) {
            return map.ceilingEntry((rand.nextDouble() * total)).getValue();
        }
    }
}