package joshie.harvest.crops;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.tile.TileWithered;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static joshie.harvest.api.crops.IStateHandler.PlantSection.BOTTOM;

public class CropHelper {
    public static final IBlockState WET_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);
    public static final IBlockState DRYING_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 3);
    public static final IBlockState DRY_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 0);
    private static final Cache<BlockPos, IBlockState> RESERVE = CacheBuilder.newBuilder().expireAfterAccess(100, TimeUnit.MILLISECONDS).maximumSize(64).build();

    public static void onBottomBroken(BlockPos pos, IBlockState state) {
        RESERVE.put(pos, state);
    }

    public static IBlockState getBlockState(IBlockAccess world, BlockPos pos, PlantSection section, boolean withered) {
        TileWithered crop = getTile(world, pos, section);
        if (section == PlantSection.TOP && crop == null) {
            IBlockState theState = RESERVE.getIfPresent(pos);
            return theState == null ? Blocks.GRASS.getDefaultState(): theState;
        }

        CropData data = crop.getData();
        return data.getCrop().getStateHandler().getState(world, pos, section, data.getStage(), withered);
    }

    public static AxisAlignedBB getCropBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, boolean withered) {
        TileWithered crop = getTile(world, pos, section);
        CropData data = crop.getData();
        return data.getCrop().getStateHandler().getBoundingBox(world, pos, section, data.getStage(), withered);
    }

    private static TileWithered getTile(IBlockAccess world, BlockPos pos, PlantSection section) {
        if (section == BOTTOM) return (TileWithered) world.getTileEntity(pos);
        else {
            TileWithered down = ((TileWithered)world.getTileEntity(pos.down()));
            return down == null ? (TileWithered) world.getTileEntity(pos): down;
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
        List<ItemStack> list = HFApi.crops.harvestCrop(player, world, pos);
        if (!world.isRemote && list != null && list.size() > 0) {
            for (ItemStack stack: list) {
                SpawnItemHelper.dropBlockAsItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }

        return list != null;
    }

    public static CropData getCropDataAt(IBlockAccess world, BlockPos pos) {
        PlantSection section = BlockHFCrops.getSection(world.getBlockState(pos));
        if (section == null) return null;
        if (section == PlantSection.BOTTOM) return ((TileWithered)world.getTileEntity(pos)).getData();
        else if (section == PlantSection.TOP) return ((TileWithered)world.getTileEntity(pos.down())).getData();
        else return null;
    }

    public static boolean isRainingAt(World world, BlockPos pos) {
        if (!HFApi.calendar.getWeather(world).isRain()) return false;
        else if (!world.canBlockSeeSky(pos)) {
            return false;
        } else if (world.getPrecipitationHeight(pos).getY() > pos.getY()) {
            return false;
        } else {
            Biome biome = world.getBiome(pos);
            return biome.canRain() || biome.isSnowyBiome();
        }
    }
}