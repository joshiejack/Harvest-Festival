package joshie.harvestmoon.calendar;

import static joshie.harvestmoon.lib.HMModInfo.MODPATH;
import joshie.lib.util.Text;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public enum Season {    
    SPRING("spring", 168000, 12000, 1, 1, Text.BRIGHT_GREEN, 0x87CEFA, 0.6082D, 0.01F, 1850L), //Flower
    SUMMER("summer", 250000, 5000, 0, 3, Text.YELLOW, 7972863, 0.1D, 0.0011F, 0L), //Sun
    AUTUMN("autumn", 120000, 13000, 3, 2, Text.ORANGE, 0x8CBED6, 1.0D, -0.0325F, 2400L), // Autumn Leaf
    WINTER("winter", 110000, 12500, 2, 1, Text.INDIGO, 0xFFFFFF, 1.35D, -0.09F, 2950L); //Snowflake
    
    private final ResourceLocation resource;
    private final int startChance;
    private final int endChance;
    private final int rain;
    private final int thunder;
    private final int color;
    private final double factor;
    private final float angle;
    private final String prefix;
    private final long sunrise;
    
    private Season(String resource, int startChance, int endChance, int rain, int thunder, String prefix, int color, double factor, float angle, long sunrise) {
        this.resource = new ResourceLocation(MODPATH, "textures/hud/" + resource + ".png");
        this.startChance = startChance;
        this.endChance = endChance;
        this.rain = rain;
        this.thunder = thunder;
        this.prefix = prefix;
        this.color = color;
        this.factor = factor;
        this.angle = angle;
        this.sunrise = sunrise;
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
        return prefix + StatCollector.translateToLocal("hm.season." + name().toLowerCase());
    }

    public long getSunrise() {
        return sunrise;
    }
}
