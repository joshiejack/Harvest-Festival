package joshie.harvest.core.helpers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.crops.blocks.TileCrop;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.api.crops.IStateHandler.PlantSection.TOP;

public class CropHelper {
    public static final IBlockState WET_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);
    public static final IBlockState DRYING_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 3);
    public static final IBlockState DRY_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 0);

    public static IBlockState getBlockState(World world, BlockPos pos, PlantSection section, boolean withered) {
        ICropData data = section == TOP ? ((TileCrop) world.getTileEntity(pos.down())).getData(): ((TileCrop) world.getTileEntity(pos)).getData();
        return data.getCrop().getStateHandler().getState(section, data.getStage(), withered);
    }

    public static AxisAlignedBB getCropBoundingBox(World world, BlockPos pos, PlantSection section, boolean withered) {
        ICropData data = section == TOP ? ((TileCrop) world.getTileEntity(pos.down())).getData(): ((TileCrop) world.getTileEntity(pos)).getData();
        if (data.getCrop() == null) return null; //Hopefully prevent a nullpointer in a race condition
        return data.getCrop().getStateHandler().getBoundingBox(section, data.getStage(), withered);
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
}