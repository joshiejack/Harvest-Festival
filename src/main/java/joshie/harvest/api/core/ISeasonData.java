package joshie.harvest.api.core;

import net.minecraft.util.ResourceLocation;

public interface ISeasonData {
    public double getCelestialLengthFactor();
    public float getCelestialAngleOffset();
    public int getRainStartChance();
    public int getRainEndChance();
    public int getRainLength();
    public int getThunderLength();
    public int getSkyColor();
    
    public ResourceLocation getResource();
    public String getLocalized();
    public String getTextColor();
}
