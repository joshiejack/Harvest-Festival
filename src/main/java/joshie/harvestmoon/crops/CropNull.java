package joshie.harvestmoon.crops;

import joshie.harvestmoon.crops.soil.SoilHandlers;


public class CropNull extends Crop {
    public CropNull() {
        unlocalized = "null_crop";
        soilHandler = SoilHandlers.farmland;
    }
}
