package joshie.harvestmoon.helpers.generic;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class MCServerHelper {
    public static World getWorld(int dimension) {
        return DimensionManager.getWorld(dimension);
    }
}
