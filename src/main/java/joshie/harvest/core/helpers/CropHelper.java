package joshie.harvest.core.helpers;

import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class CropHelper {
    public static IBlockState getBlockState(World world, BlockPos pos, PlantSection section, boolean withered) {
        ICropData data = HFTrackers.getCropTracker().getCropDataForLocation(world, pos);
        return data.getCrop().getStateHandler().getState(section, data.getStage(), withered);
    }

    public static AxisAlignedBB getCropBoundingBox(World world, BlockPos pos, PlantSection section, boolean withered) {
        ICropData data = HFTrackers.getCropTracker().getCropDataForLocation(world, pos);
        return data.getCrop().getStateHandler().getBoundingBox(section, data.getStage(), withered);
    }

    //Converts the soil to hydrated soil
    public static boolean hydrate(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        int meta = state.getValue(BlockFarmland.MOISTURE);
        boolean ret = meta != 7 && world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 7), 2);
        if (ret) {
            HFTrackers.getCropTracker().hydrate(world, pos.up(), state);
        }
        return ret;
    }

    //Returns false if the soil is no longer farmland and should be converted to dirt
    public static boolean dehydrate(World world, BlockPos pos, IBlockState state) {
        Block crop = world.getBlockState(pos.up()).getBlock();
        Block farmland = state.getBlock();
        if (!(farmland instanceof BlockFarmland)) return true;
        int meta = state.getValue(BlockFarmland.MOISTURE);
        if (crop instanceof IPlantable && farmland.canSustainPlant(state, world, pos, EnumFacing.UP, (IPlantable) crop)) {
            world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 0), 2);
            return true;
        } else if (meta == 7) {
            world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 0), 2);
            return true;
        } else {
            return false;
        }
    }

    //Returns whether the farmland is hydrated
    public static boolean isHydrated(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockFarmland && state.getValue(BlockFarmland.MOISTURE) == 7;
    }

    //Harvests the crop at this location
    public static boolean harvestCrop(EntityPlayer player, World world, BlockPos pos) {
        ItemStack stack = HFTrackers.getCropTracker().harvest(player, world, pos);
        if (!world.isRemote && stack != null) {
            ItemHelper.dropBlockAsItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }

        return stack != null;
    }
}