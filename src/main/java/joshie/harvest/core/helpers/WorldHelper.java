package joshie.harvest.core.helpers;

import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WorldHelper {
    public static World getWorld(IBlockAccess access) {
        if (access instanceof ChunkCache) {
            return MCClientHelper.getWorld();
        } else if (access instanceof World) {
            return ((World)access);
        } else return null;
    }
}
