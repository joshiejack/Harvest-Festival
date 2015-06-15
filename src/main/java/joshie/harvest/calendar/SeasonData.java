package joshie.harvest.calendar;

import static joshie.harvest.core.lib.HFModInfo.MODPATH;
import static joshie.harvest.core.util.generic.Text.BRIGHT_GREEN;
import static joshie.harvest.core.util.generic.Text.INDIGO;
import static joshie.harvest.core.util.generic.Text.ORANGE;
import static joshie.harvest.core.util.generic.Text.YELLOW;

import java.util.EnumMap;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class SeasonData implements ISeasonData {
    private static final EnumMap<Season, SeasonData> data = new EnumMap(Season.class);
    static {
        data.put(Season.SPRING, new SeasonData(Season.SPRING, 168000, 12000, 1, 1, 0x87CEFA, 0.6082D, 0.01F, 1850L, BRIGHT_GREEN, 0x00D900));
        data.put(Season.SUMMER, new SeasonData(Season.SUMMER, 250000, 5000, 0, 3, 7972863, 0.1D, 0.0011F, 0L, YELLOW, 0xFFFF4D));
        data.put(Season.AUTUMN, new SeasonData(Season.AUTUMN, 120000, 13000, 3, 2, 0x8CBED6, 1.0D, -0.0325F, 2400L, ORANGE, 0x8C4600));
        data.put(Season.WINTER, new SeasonData(Season.WINTER, 110000, 12500, 2, 1, 0xFFFFFF, 1.35D, -0.09F, 2950L, INDIGO, 0xFFFFFF));
    }

    public static SeasonData getData(Season season) {
        return data.get(season);
    }

    public final Season season;
    private final ResourceLocation resource;
    private final int rainStartChance;
    private final int rainEndChance;
    private final int rainLength;
    private final int thunderLength;
    private final int skyColor;
    private final double celestialLengthFactor;
    private final float celestialAngleOffset;
    public final long sunrise;
    public final String textColor;
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
}
