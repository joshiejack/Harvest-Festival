package joshie.harvest.crops.handlers;

import joshie.harvest.api.crops.ISoilHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.EnumPlantType;

public class SoilHandlers {
    public static final ISoilHandler farmland = new SoilHandlerDefault(EnumPlantType.Crop);
    public static final ISoilHandler sand = new SoilHandlerDefault(EnumPlantType.Desert);
    public static final ISoilHandler mushroom = new SoilHandlerDefault(EnumPlantType.Cave, Blocks.MYCELIUM);
    public static final ISoilHandler sugarcane = new SoilHandlerDefault(EnumPlantType.Water);
}