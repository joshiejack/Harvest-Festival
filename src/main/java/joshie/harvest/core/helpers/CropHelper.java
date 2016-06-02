package joshie.harvest.core.helpers;

import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.ItemHelper;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CropHelper {
    public static final IBlockState WET_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);
    public static final IBlockState DRY_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 0);

    public static IBlockState getBlockState(World world, BlockPos pos, PlantSection section, boolean withered) {
        ICropData data = HFTrackers.getCropTracker(world).getCropDataForLocation(pos);
        return data.getCrop().getStateHandler().getState(section, data.getStage(), withered);
    }

    public static AxisAlignedBB getCropBoundingBox(World world, BlockPos pos, PlantSection section, boolean withered) {
        ICropData data = HFTrackers.getCropTracker(world).getCropDataForLocation(pos);
        return data.getCrop().getStateHandler().getBoundingBox(section, data.getStage(), withered);
    }

    //Converts the soil to hydrated soil
    public static boolean hydrate(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        boolean ret = !isHydrated(world, pos);
        if (ret) {
            HFTrackers.getCropTracker(world).hydrate(pos.up(), state);
            world.setBlockState(pos, WET_SOIL);
        }

        return ret;
    }

    //Returns whether the farmland is hydrated
    public static boolean isHydrated(World world, BlockPos pos) {
        return world.getBlockState(pos) == WET_SOIL;
    }

    //Harvests the crop at this location
    public static boolean harvestCrop(EntityPlayer player, World world, BlockPos pos) {
        ItemStack stack = HFTrackers.getCropTracker(world).harvest(player, pos);
        if (!world.isRemote && stack != null) {
            ItemHelper.dropBlockAsItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }

        return stack != null;
    }
}