package joshie.harvest.core.config;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.Crop;

import java.util.HashMap;

public class Mappings {
    private HashMap<String, ICrop> cropUnlocalizedMappings = new HashMap<String, ICrop>();

    public void addCrop(Crop crop) {
        cropUnlocalizedMappings.put(crop.getUnlocalizedName(), crop);
    }

    public ICrop getCrop(String unlocalized) {
        return cropUnlocalizedMappings.get(unlocalized);
    }
}