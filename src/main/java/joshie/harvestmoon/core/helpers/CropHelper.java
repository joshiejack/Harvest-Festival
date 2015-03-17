package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.core.helpers.generic.ItemHelper;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.crops.CropTrackerClient;
import joshie.harvestmoon.crops.CropTrackerCommon;
import joshie.harvestmoon.crops.CropTrackerServer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class CropHelper {
    //Converts the soil to hydrated soil
    public static boolean hydrate(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        boolean ret = meta == 7 ? false : world.setBlockMetadataWithNotify(x, y, z, 7, 2);
        if (ret) {
            getTracker(world).hydrate(world, x, y + 1, z);
        }

        return ret;
    }

    //Returns false if the soil is no longer farmland and should be converted to dirt
    public static boolean dehydrate(World world, int x, int y, int z) {
        Block crop = world.getBlock(x, y + 1, z);
        Block farmland = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (!(farmland instanceof BlockFarmland)) return true;
        else if (crop instanceof IPlantable && farmland.canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable) crop)) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            return true;
        } else if (meta == 7) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            return true;
        } else {
            return false;
        }
    }

    //Returns whether the farmland is hydrated
    public static boolean isHydrated(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) instanceof BlockFarmland && world.getBlockMetadata(x, y, z) == 7;
    }

    //Fetch the crop data at this location
    public static ICropData getCropAtLocation(World world, int x, int y, int z) {
        return getTracker(world).getCropDataForLocation(world, x, y, z);
    }

    //Remove the crop data at this location
    public static void removeCrop(World world, int x, int y, int z) {
        getTracker(world).removeCrop(world, x, y, z);
    }

    //Set some crop data at this location
    public static boolean plantCrop(EntityPlayer player, World world, int x, int y, int z, ICrop crop, int quality) {
        return plantCrop(player, world, x, y, z, crop, quality, 1);
    }

    //Set some crop data at this location
    public static boolean plantCrop(EntityPlayer player, World world, int x, int y, int z, ICrop crop, int quality, int regrowStage) {
        return getTracker(world).plantCrop(player, world, x, y, z, crop, quality, regrowStage);
    }

    //Harvests the crop at this location
    public static boolean harvestCrop(EntityPlayer player, World world, int x, int y, int z) {
        ItemStack stack = getTracker(world).harvest(player, world, x, y, z);
        if (!world.isRemote && stack != null) {
            ItemHelper.dropBlockAsItem(world, x, y, z, stack);
        }

        return stack != null;
    }

    //Whether or not you can bonemeal this location
    public static boolean canBonemeal(World world, int x, int y, int z) {
        return getTracker(world).canBonemeal(world, x, y, z);
    }

    public static int getCropQualityFromDamage(int meta) {
        return 1 + (int) Math.ceil(meta / 100);
    }

    private static CropTrackerCommon getTracker(World world) {
        return world.isRemote ? getClientTracker() : getServerTracker();
    }

    public static CropTrackerServer getServerTracker() {
        return ServerHelper.getCropTracker();
    }

    private static CropTrackerClient getClientTracker() {
        return ClientHelper.getCropTracker();
    }

    public static void newDay() {
        ServerHelper.getCropTracker().newDay();
    }

    public static void grow(World world, int x, int y, int z) {
        getServerTracker().grow(world, x, y, z);
    }
}
