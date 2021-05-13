package uk.joshiejack.seasons.data;

import uk.joshiejack.seasons.Season;
import net.minecraft.util.text.TextFormatting;

import java.util.EnumMap;

public class SeasonData {
    public static final EnumMap<Season, SeasonData> DATA = new EnumMap<>(Season.class);
    public final TextFormatting hud;
    public final int leaves;
    public final int grass;
    public final int sky;
    public final long sunrise;
    public final long sunset;
    public final float temperature;

    public SeasonData(TextFormatting hud, float temperature, int leaves, int grass, int sky, long sunrise, long sunset) {
        this.hud = hud;
        this.temperature = temperature;
        this.leaves = leaves;
        this.sky = sky;
        this.grass = grass;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }
}
