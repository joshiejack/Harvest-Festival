package joshie.harvest.core.config;

import com.google.gson.annotations.SerializedName;

/* This class is loaded via json for the config */
public class ASM {
    @SerializedName("Snow > Enable Override")
    public boolean SNOW_OVERRIDE = true;

    @SerializedName("Rain > Fix Particles when Snowing")
    public boolean RAIN_OVERRIDE = true;
}