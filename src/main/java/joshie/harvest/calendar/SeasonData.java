package joshie.harvest.calendar;

import static joshie.harvest.core.lib.HFModInfo.MODPATH;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class SeasonData implements ISeasonData {
    public final Season season;
    private final ResourceLocation resource;
    private final int rainStartChance;
    private final int rainEndChance;
    private final int rainLength;
    private final int thunderLength;
    private final int skyColor;
    private final double celestialLengthFactor;
    private final float celestialAngleOffset;
    private final String textColor;
    public final long sunrise;
    public final int seasonColor;

    public SeasonData(Season season, int startChance, int endChance, int rain, int thunder, int color, double factor, float angle, long sunrise, String textColor, int seasonColor) {
        this.season = season;
        this.resource = new ResourceLocation(MODPATH, "textures/hud/" + season.name().toLowerCase() + ".png");
        this.rainStartChance = startChance;
        this.rainEndChance = endChance;
        this.rainLength = rain;
        this.thunderLength = thunder;
        this.skyColor = color;
        this.celestialLengthFactor = factor;
        this.celestialAngleOffset = angle;
        this.sunrise = sunrise;
        this.textColor = textColor;
        this.seasonColor = seasonColor;
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
    public int getRainStartChance() {
        return rainStartChance;
    }

    @Override
    public int getRainEndChance() {
        return rainEndChance;
    }

    @Override
    public int getRainLength() {
        return rainLength;
    }

    @Override
    public int getThunderLength() {
        return thunderLength;
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
