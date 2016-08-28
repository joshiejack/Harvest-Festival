package joshie.harvest.core;

import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.api.core.IDailyTickableBlock;
import joshie.harvest.api.core.IDailyTickableRegistry;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.HFApiImplementation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

import static joshie.harvest.crops.CropHelper.DRYING_SOIL;
import static joshie.harvest.crops.CropHelper.DRY_SOIL;
import static joshie.harvest.crops.CropHelper.WET_SOIL;

@HFApiImplementation
public class HFDailyTickable implements IDailyTickableRegistry {
    public static final HFDailyTickable INSTANCE = new HFDailyTickable();
    private final HashMap<Block, IDailyTickableBlock> registry = new HashMap<>();

    private HFDailyTickable() {
        registry.put(Blocks.FARMLAND, new IDailyTickableBlock() {
            private boolean hasCrops(World worldIn, BlockPos pos)  {
                Block block = worldIn.getBlockState(pos.up()).getBlock();
                return block instanceof net.minecraftforge.common.IPlantable;
            }

            @Override
            public boolean newDay(World world, BlockPos pos, IBlockState state) {
                if (state != WET_SOIL && world.isRainingAt(pos.up(2))) {
                    world.setBlockState(pos, WET_SOIL, 2);
                } else {
                    if (state == WET_SOIL) world.setBlockState(pos, DRYING_SOIL, 2);
                    else if (state == DRYING_SOIL) world.setBlockState(pos, DRY_SOIL, 2);
                    else if (state == DRY_SOIL && (!hasCrops(world, pos))) world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
                }

                return world.getBlockState(pos).getBlock() == DRY_SOIL.getBlock();
            }
        });
    }

    @Override
    public void registerDailyTickableBlock(Block block, IDailyTickableBlock daily) {
        registry.put(block, daily);
    }

    @Override
    public void addTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            HFTrackers.getTickables(world).add(tickable);
        }
    }

    @Override
    public void addTickable(World world, BlockPos pos, IDailyTickableBlock tickable) {
        if (!world.isRemote) {
            HFTrackers.getTickables(world).add(pos, tickable);
        }
    }

    @Override
    public void removeTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            HFTrackers.getTickables(world).remove(tickable);
        }
    }

    @Override
    public void removeTickable(World world, BlockPos pos) {
        if (!world.isRemote) {
            HFTrackers.getTickables(world).remove(pos);
        }
    }

    @Override
    public IDailyTickableBlock getTickableFromBlock(Block block) {
        if (block instanceof IDailyTickableBlock) {
            return ((IDailyTickableBlock)block);
        } else return registry.get(block);
    }
}
