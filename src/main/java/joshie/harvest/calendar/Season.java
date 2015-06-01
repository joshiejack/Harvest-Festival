package joshie.harvest.calendar;

import static joshie.harvest.core.lib.HFModInfo.MODPATH;
import static joshie.harvest.core.util.generic.Text.BRIGHT_GREEN;
import static joshie.harvest.core.util.generic.Text.INDIGO;
import static joshie.harvest.core.util.generic.Text.ORANGE;
import static joshie.harvest.core.util.generic.Text.YELLOW;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public enum Season {
    SPRING("spring", 168000, 12000, 1, 1, 0x87CEFA, 0.6082D, 0.01F, 1850L, BRIGHT_GREEN, 0x00D900), //Flower
    SUMMER("summer", 250000, 5000, 0, 3, 7972863, 0.1D, 0.0011F, 0L, YELLOW, 0xFFFF4D), //Sun
    AUTUMN("autumn", 120000, 13000, 3, 2, 0x8CBED6, 1.0D, -0.0325F, 2400L, ORANGE, 0x8C4600), // Autumn Leaf
    WINTER("winter", 110000, 12500, 2, 1, 0xFFFFFF, 1.35D, -0.09F, 2950L, INDIGO, 0xFFFFFF); //Snowflake

    private final ResourceLocation resource;
    private final int startChance;
    private final int endChance;
    private final int rain;
    private final int thunder;
    private final int color;
    private final double factor;
    private final float angle;
    private final long sunrise;
    private final String textColor;
    private final int seasonColor;

    private Season(String resource, int startChance, int endChance, int rain, int thunder, int color, double factor, float angle, long sunrise, String textColor, int seasonColor) {
        this.resource = new ResourceLocation(MODPATH, "textures/hud/" + resource + ".png");
        this.startChance = startChance;
        this.endChance = endChance;
        this.rain = rain;
        this.thunder = thunder;
        this.color = color;
        this.factor = factor;
        this.angle = angle;
        this.sunrise = sunrise;
        this.textColor = textColor;
        this.seasonColor = seasonColor;
    }

    public ResourceLocation getTexture() {
        return resource;
    }

    public int getStartChance() {
        return startChance;
    }

    public int getEndChance() {
        return endChance;
    }

    public int getThunderTick() {
        return thunder;
    }

    public int getRainTick() {
        return rain;
    }

    public int getSkyColor() {
        return color;
    }

    public double getLengthFactor() {
        return factor;
    }

    public float getAngleOffset() {
        return angle;
    }

    public String getLocalized() {
        return StatCollector.translateToLocal(HFModInfo.MODPATH + ".season." + name().toLowerCase());
    }

    public String getTextColor() {
        return textColor;
    }

    public long getSunrise() {
        return sunrise;
    }

    public int getColor() {
        return seasonColor;
    }
}
