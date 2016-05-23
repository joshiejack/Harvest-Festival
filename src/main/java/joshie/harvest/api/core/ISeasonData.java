package joshie.harvest.api.core;

import joshie.harvest.api.calendar.Weather;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public interface ISeasonData {
    double getCelestialLengthFactor();
    float getCelestialAngleOffset();
    double getWeatherChance(Weather weather);
    int getSkyColor();
    
    ResourceLocation getResource();
    String getLocalized();
    TextFormatting getTextColor();
}