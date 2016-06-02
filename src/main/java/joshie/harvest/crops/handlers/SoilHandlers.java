package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.ISoilHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.EnumPlantType;

public class SoilHandlers {
    public static final ISoilHandler FARMLAND = new SoilHandlerDefault(EnumPlantType.Crop, Blocks.FARMLAND);
    public static final ISoilHandler SOUL_SAND = new SoilHandlerDefault(EnumPlantType.Nether, Blocks.SOUL_SAND);
}