package joshie.harvestmoon.crops.soil;

import net.minecraftforge.common.EnumPlantType;

public class SoilHandlers {
    public static final SoilHandlerDefault farmland = new SoilHandlerDefault(EnumPlantType.Crop);
    public static final SoilHandlerDefault sand = new SoilHandlerDefault(EnumPlantType.Desert);
}
