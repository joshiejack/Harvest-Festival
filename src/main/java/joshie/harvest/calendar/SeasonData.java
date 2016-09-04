package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class SeasonData {
    private final ResourceLocation resource;
    private final int skyColor;
    private final double celestialLengthFactor;
    private final float celestialAngleOffset;
    private final double[] chances;

    public SeasonData(Season season, int color, double factor, float angle, double sunny, double rain, double typhoon, double snow, double blizzard) {
        this.resource = new ResourceLocation(MODID, "textures/hud/" + season.name().toLowerCase(Locale.ENGLISH) + ".png");
        this.skyColor = color;
        this.celestialLengthFactor = factor;
        this.celestialAngleOffset = angle;
        this.chances = new double[5];
        this.chances[0] = sunny;
        this.chances[1] = rain;
        this.chances[2] = typhoon;
        this.chances[3] = snow;
        this.chances[4] = blizzard;
    }

    public double getCelestialLengthFactor() {
        return celestialLengthFactor;
    }

    public float getCelestialAngleOffset() {
        return celestialAngleOffset;
    }

    public double getWeatherChance(Weather weather) {
        return chances[weather.ordinal()];
    }

    public int getSkyColor() {
        return skyColor;
    }

    public ResourceLocation getResource() {
        return resource;
    }
}