package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.tile.TileCrop;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.api.crops.IStateHandler.PlantSection.BOTTOM;

public class CropHelper {
    public static final IBlockState WET_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);
    public static final IBlockState DRYING_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 3);
    public static final IBlockState DRY_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 0);

    public static IBlockState getBlockState(World world, BlockPos pos, PlantSection section, boolean withered) {
        CropData data = getTile(world, pos, section).getData();
        return data.getCrop().getStateHandler().getState(section, data.getStage(), withered);
    }

    public static AxisAlignedBB getCropBoundingBox(World world, BlockPos pos, PlantSection section, boolean withered) {
        CropData data = getTile(world, pos, section).getData();
        return data.getCrop().getStateHandler().getBoundingBox(section, data.getStage(), withered);
    }

    private static TileCrop getTile(World world, BlockPos pos, PlantSection section) {
        if (section == BOTTOM) return (TileCrop) world.getTileEntity(pos);
        else {
            TileCrop down = ((TileCrop)world.getTileEntity(pos.down()));
            return down == null ? (TileCrop) world.getTileEntity(pos): down;
        }
    }

    //Returns whether the farmland is hydrated
    public static boolean isWetSoil(IBlockState state) {
        return state == WET_SOIL;
    }

    public static boolean isSoil(IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND;
    }

    //Harvests the crop at this location
    public static boolean harvestCrop(EntityPlayer player, World world, BlockPos pos) {
        ItemStack stack = HFApi.crops.harvestCrop(player, world, pos);
        if (!world.isRemote && stack != null) {
            ItemHelper.dropBlockAsItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }

        return stack != null;
    }

    public static CropData getCropDataAt(World world, BlockPos pos) {
        PlantSection section = BlockHFCrops.getSection(world.getBlockState(pos));
        if (section == null) return null;
        if (section == PlantSection.BOTTOM) return ((TileCrop)world.getTileEntity(pos)).getData();
        else if (section == PlantSection.TOP) return ((TileCrop)world.getTileEntity(pos.down())).getData();
        else return null;
    }

    public static boolean isRainingAt(World world, BlockPos pos) {
        if (!HFApi.calendar.getWeather(world).isRain()) return false;
        else if (!world.canSeeSky(pos)) {
            return false;
        } else if (world.getPrecipitationHeight(pos).getY() > pos.getY()) {
            return false;
        } else {
            return world.getBiome(pos).canRain();
        }
    }
}