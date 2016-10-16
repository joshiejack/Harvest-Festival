package joshie.harvest.crops;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.api.crops.WateringHandler;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.tile.TileWithered;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
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

    public static IBlockState getTempState(BlockPos pos) {
        return RESERVE.getIfPresent(pos);
    }

    public static TileWithered getTile(IBlockAccess world, BlockPos pos, PlantSection section) {
        if (section == BOTTOM) return (TileWithered) world.getTileEntity(pos);
        else {
            TileWithered down = ((TileWithered)world.getTileEntity(pos.down()));
            return down == null ? (TileWithered) world.getTileEntity(pos): down;
        }
    }

    //Returns whether the farmland is hydrated
    public static boolean isWetSoil(IBlockState state) {
        WateringHandler handler = getWateringHandler(state);
        return handler != null && handler.isWet(state);
    }

    //Returns true if this is waterable
    public static WateringHandler getWateringHandler(IBlockState state) {
        for (WateringHandler checker: CropRegistry.INSTANCE.wateringHandlers) {
            if (checker.canBeWatered(state)) return checker;
        }

        return null;
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

    @Nullable
    public static CropData getCropDataAt(IBlockAccess world, BlockPos pos) {
        PlantSection section = BlockHFCrops.getSection(world.getBlockState(pos));
        if (section == PlantSection.BOTTOM) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileWithered) return ((TileWithered)tile).getData();
        } else if (section == PlantSection.TOP) {
            TileEntity tile = world.getTileEntity(pos.down());
            if (tile instanceof TileWithered) return ((TileWithered)tile).getData();
        }

        return null;
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