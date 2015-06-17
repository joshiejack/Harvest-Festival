package joshie.harvest.core.config;

import com.google.gson.annotations.SerializedName;

/* This class is loaded via json for the config */
public class ASM {
    /* Egg Config Options */
    @SerializedName("Egg > Enable Override")
    public boolean EGG_OVERRIDE = true;
    @SerializedName("Egg > Disable Throwing")
    public boolean EGG_DISABLE_THROWING = true;

    /* Easy Planting Options */
    @SerializedName("Carrot/Potato > Disable Easy Planting")
    public boolean CARROT_POTATO_DISABLE_PLANTING = true;

    /* Carrot Options */
    @SerializedName("Carrot > Enable Override")
    public boolean CARROT_OVERRIDE = true;
    @SerializedName("Carrot Block > Disable Ticking")
    public boolean CARROT_BLOCK_DISABLE_TICKING = true;

    /* Potato Options */
    @SerializedName("Potato > Enable Override")
    public boolean POTATO_OVERRIDE = true;
    @SerializedName("Potato Block > Disable Ticking")
    public boolean POTATO_BLOCK_DISABLE_TICKING = true;

    /* Wheat Options */
    @SerializedName("Wheat > Enable Override")
    public boolean WHEAT_OVERRIDE = true;
    @SerializedName("Wheat Block > Disable Ticking")
    public boolean WHEAT_BLOCK_DISABLE_TICKING = true;
    
    /* Pumpkin Options */
    @SerializedName("Pumpkin > Enable Override")
    public boolean PUMPKIN_OVERRIDE = true;
    @SerializedName("Pumpkin Block > Disable Ticking")
    public boolean PUMPKIN_BLOCK_DISABLE_TICKING = true;
    
    /* Watermelon Options */
    @SerializedName("Watermelon > Enable Override")
    public boolean WATERMELON_OVERRIDE = true;
    @SerializedName("Watermelon Block > Disable Ticking")
    public boolean WATERMELON_BLOCK_DISABLE_TICKING = true;
    
    /* Harvestcraft Options */
    //TODO: Future Plugins
   //@SerializedName("Harvestcraft > Enable Override")
   // public boolean OVERRIDE_HARVESTCRAFT = true;

    /* Farmland and SNow Options */
    @SerializedName("Snow > Enable Override")
    public boolean SNOW_OVERRIDE = true;
    @SerializedName("Farmland > Enable Override")
    public boolean FARMLAND_OVERRIDE = true;

    /* Tab Options */
    @SerializedName("Overrides > Move Tabs")
    public boolean MOVE_OVERRIDE_TAB = true;
}