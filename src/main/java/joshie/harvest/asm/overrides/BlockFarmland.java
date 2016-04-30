package joshie.harvest.asm.overrides;

import joshie.harvest.api.WorldLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.WeakHashMap;

public class BlockFarmland {
    public static WeakHashMap<WorldLocation, Long> timePlaced = new WeakHashMap<WorldLocation, Long>();

    public static void tick(World world, BlockPos pos, IBlockState state) {
        IBlockState stateAbove = world.getBlockState(pos.up());
        if (!(stateAbove.getBlock() instanceof IPlantable)) {
            if (!world.isRaining()) {
                int meta = state.getValue(net.minecraft.block.BlockFarmland.MOISTURE);
                if (meta == 7) {
                    world.setBlockState(pos, state.withProperty(net.minecraft.block.BlockFarmland.MOISTURE, 0), 2);
                } else {
                    WorldLocation location = new WorldLocation(world.provider.getDimension(), pos);
                    Long time = timePlaced.get(location);
                    if (time == null) {
                        timePlaced.put(location, System.currentTimeMillis());
                    } else if (System.currentTimeMillis() - time >= 60000) {
                        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                    }
                }
            } else {
                world.setBlockState(pos, state.withProperty(net.minecraft.block.BlockFarmland.MOISTURE, 7), 2);
            }
        }
    }
}