package joshie.harvestmoon.config;

import com.google.gson.annotations.SerializedName;

/* This class is loaded via json for the config */
public class Vanilla {
    @SerializedName("Egg > Enable Override")
    public boolean EGG_OVERRIDE = true;
    @SerializedName("Egg > Disable Throwing")
    public boolean EGG_DISABLE_THROWING = true;
    @SerializedName("Snow > Enable Override")
    public boolean SNOW_OVERRIDE = true;
    @SerializedName("Farmland > Enable Override")
    public boolean FARMLAND_OVERRIDE = true;
    @SerializedName("Potato > Enable Override")
    public boolean POTATO_OVERRIDE = true;
    @SerializedName("Carrot > Enable Override")
    public boolean CARROT_OVERRIDE = true;
    @SerializedName("Carrot/Potato > Disable Easy Planting")
    public boolean CARROT_POTATO_DISABLE_PLANTING = true;
    @SerializedName("Carrot Block > Disable Ticking")
    public boolean CARROT_BLOCK_DISABLE_TICKING = true;
    @SerializedName("Potato Block > Disable Ticking")
    public boolean POTATO_BLOCK_DISABLE_TICKING = true;
    @SerializedName("Overrides > Move Tabs")
    public boolean MOVE_OVERRIDE_TAB = true;
    @SerializedName("Hoes > Useless")
    public boolean HOES_ARE_USELESS = true;
    @SerializedName("Hoes > Hide")
    public boolean HOES_HIDDEN = true;
    
}