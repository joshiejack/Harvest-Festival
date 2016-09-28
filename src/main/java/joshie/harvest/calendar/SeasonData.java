package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import net.minecraft.util.ResourceLocation;

import java.util.EnumMap;
import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class SeasonData {
    private final ResourceLocation resource;
    private final int skyColor, grassColor, leavesColor;
    private final double celestialLengthFactor;
    private final float celestialAngleOffset;
    private final EnumMap<Weather, Double> chances = new EnumMap<>(Weather.class);

    public SeasonData(Season season, int skyColor, int grassColor, int leavesColor, double factor, float angle, double sunny, double rain, double typhoon, double snow, double blizzard) {
        this.resource = new ResourceLocation(MODID, "textures/hud/" + season.name().toLowerCase(Locale.ENGLISH) + ".png");
        this.skyColor = skyColor;
        this.grassColor = grassColor;
        this.leavesColor = leavesColor;
        this.celestialLengthFactor = factor;
        this.celestialAngleOffset = angle;
        chances.put(Weather.SUNNY, sunny);
        chances.put(Weather.RAIN, rain);
        chances.put(Weather.TYPHOON, typhoon);
        chances.put(Weather.SNOW, snow);
        chances.put(Weather.BLIZZARD, blizzard);
    }

    public double getCelestialLengthFactor() {
        return celestialLengthFactor;
    }

    public float getCelestialAngleOffset() {
        return celestialAngleOffset;
    }

    public double getWeatherChance(Weather weather) {
        return chances.get(weather);
    }

    public int getSkyColor() {
        return skyColor;
    }

    public int getGrassColor() {
        return grassColor;
    }

    public int getLeavesColor() {
        return leavesColor;
    }

    public ResourceLocation getResource() {
        return resource;
    }
}