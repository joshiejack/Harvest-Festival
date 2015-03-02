package joshie.harvestmoon.api.crops;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public interface ISoilHandler {
    public boolean canSustainPlant(IBlockAccess access, int x, int y, int z, IPlantable plantable);
}
