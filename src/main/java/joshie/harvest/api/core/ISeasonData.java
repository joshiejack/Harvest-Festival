package joshie.harvest.api.core;

import joshie.harvest.api.calendar.Weather;
import net.minecraft.util.ResourceLocation;

public interface ISeasonData {
    public double getCelestialLengthFactor();
    public float getCelestialAngleOffset();
    public double getWeatherChance(Weather weather);
    public int getSkyColor();
    
    public ResourceLocation getResource();
    public String getLocalized();
    public String getTextColor();
}