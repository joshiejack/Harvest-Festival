package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.ISoilHandler;
import joshie.harvest.crops.HFCrops;
import net.minecraftforge.common.EnumPlantType;

public class SoilHandlers {
    public static final ISoilHandler farmland = new SoilHandlerDefault(EnumPlantType.Crop, HFCrops.FARMLAND);
}