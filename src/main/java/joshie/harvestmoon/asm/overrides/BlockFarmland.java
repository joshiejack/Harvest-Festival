package joshie.harvestmoon.asm.overrides;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockFarmland {
    public static void tick(World world, int x, int y, int z) {
        Block above = world.getBlock(x, y + 1, z);
        if (!(above instanceof IPlantable) && !world.isRaining()) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta == 7) {
                world.setBlockMetadataWithNotify(x, y, z, 0, 3);
            } else world.setBlock(x, y, z, Blocks.dirt);
        }
    }
}
