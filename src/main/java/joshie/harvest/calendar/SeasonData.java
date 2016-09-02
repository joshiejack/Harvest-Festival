package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class SeasonData {
    public final Season season;
    private final ResourceLocation resource;
    private final int skyColor;
    private final double celestialLengthFactor;
    private final float celestialAngleOffset;
    private final double[] chances;
    private final TextFormatting textColor;

    public SeasonData(Season season, int color, double factor, float angle, TextFormatting textColor, double sunny, double rain, double typhoon, double snow, double blizzard) {
        this.season = season;
        this.resource = new ResourceLocation(MODID, "textures/hud/" + season.name().toLowerCase(Locale.US) + ".png");
        this.skyColor = color;
        this.celestialLengthFactor = factor;
        this.celestialAngleOffset = angle;
        this.textColor = textColor;
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

    @SuppressWarnings("deprecation")
    public String getLocalized() {
        return I18n.translateToLocal(MODID + ".season." + season.name().toLowerCase(Locale.US));
    }

    public TextFormatting getTextColor() {
        return textColor;
    }
}