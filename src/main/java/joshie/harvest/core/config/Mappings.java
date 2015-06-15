package joshie.harvest.core.config;

import java.util.HashMap;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.Crop;

public class Mappings {    
    private HashMap<String, ICrop> cropUnlocalizedMappings = new HashMap();

    public void addCrop(Crop crop) {
        cropUnlocalizedMappings.put(crop.getUnlocalizedName(), crop);
    }
    public ICrop getCrop(String unlocalized) {
        return cropUnlocalizedMappings.get(unlocalized);
    }
}
