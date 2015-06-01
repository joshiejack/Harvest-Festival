package joshie.harvest.asm.overrides;

import java.util.WeakHashMap;

import joshie.harvest.api.WorldLocation;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockFarmland {
    public static WeakHashMap<WorldLocation, Long> timePlaced = new WeakHashMap();

    public static void tick(World world, int x, int y, int z) {
        Block above = world.getBlock(x, y + 1, z);
        if (!(above instanceof IPlantable) && !world.isRaining()) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta == 7) {
                world.setBlockMetadataWithNotify(x, y, z, 0, 3);
            } else {
                WorldLocation location = new WorldLocation(world.provider.dimensionId, x, y, z);
                Long time = timePlaced.get(location);
                if (time == null) {
                    timePlaced.put(location, System.currentTimeMillis());
                } else if (System.currentTimeMillis() - time >= 60000) {
                    world.setBlock(x, y, z, Blocks.dirt);
                }
            }
        }
    }
}
