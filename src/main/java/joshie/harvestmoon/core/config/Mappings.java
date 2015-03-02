package joshie.harvestmoon.core.config;

import java.util.HashMap;
import java.util.HashSet;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.crops.Crop;

import com.google.gson.annotations.Expose;

public class Mappings {    
    private HashMap<String, ICrop> cropUnlocalizedMappings = new HashMap();

    public void addCrop(Crop crop) {
        cropUnlocalizedMappings.put(crop.getUnlocalizedName(), crop);
    }
    public ICrop getCrop(String unlocalized) {
        return cropUnlocalizedMappings.get(unlocalized);
    }
}
