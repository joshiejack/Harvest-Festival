package joshie.harvest.calendar;

import static joshie.harvest.core.lib.HFModInfo.MODPATH;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class SeasonData implements ISeasonData {
    public final Season season;
    private final ResourceLocation resource;
    private final int skyColor;
    private final double celestialLengthFactor;
    private final float celestialAngleOffset;
    private final double[] chances;
    private final String textColor;
    public final long sunrise;
    public final int seasonColor;

    public SeasonData(Season season, int color, double factor, float angle, long sunrise, String textColor, int seasonColor, double sunny, double rain, double drizzle, double typhoon, double snow, double blizzard) {
        this.season = season;
        this.resource = new ResourceLocation(MODPATH, "textures/hud/" + season.name().toLowerCase() + ".png");
        this.skyColor = color;
        this.celestialLengthFactor = factor;
        this.celestialAngleOffset = angle;
        this.sunrise = sunrise;
        this.textColor = textColor;
        this.seasonColor = seasonColor;
        this.chances = new double[6];
        this.chances[0] = sunny;
        this.chances[1] = drizzle;
        this.chances[1] = rain;
        this.chances[2] = typhoon;
        this.chances[3] = snow;
        this.chances[4] = blizzard;
    }
    
    @Override
    public double getCelestialLengthFactor() {
        return celestialLengthFactor;
    }

    @Override
    public float getCelestialAngleOffset() {
        return celestialAngleOffset;
    }
    
    @Override
    public double getWeatherChance(Weather weather) {
        return chances[weather.ordinal()];
    }

    @Override
    public int getSkyColor() {
        return skyColor;
    }
    
    @Override
    public ResourceLocation getResource() {
        return resource;
    }

    @Override
    public String getLocalized() {
        return StatCollector.translateToLocal(HFModInfo.MODPATH + ".season." + season.name().toLowerCase());
    }

    @Override
    public String getTextColor() {
        return textColor;
    }
}
