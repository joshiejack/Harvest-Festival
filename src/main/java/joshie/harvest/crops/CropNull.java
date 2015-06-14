package joshie.harvest.crops;

import net.minecraft.item.ItemStack;



public class CropNull extends Crop {
    public CropNull() {
        unlocalized = "null_crop";
        soilHandler = SoilHandlers.farmland;
    }
    
    @Override
    public ItemStack getCropStack() {
        return null;
    }
}
