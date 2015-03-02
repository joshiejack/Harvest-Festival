package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.core.helpers.generic.ItemHelper;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.crops.CropData;
import joshie.harvestmoon.crops.CropTrackerClient;
import joshie.harvestmoon.crops.CropTrackerCommon;
import joshie.harvestmoon.crops.CropTrackerServer;
import joshie.harvestmoon.init.HMConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class CropHelper {
    public static void removeCrop(World world, int x, int y, int z) {
        getTracker(world).removeCrop(world, x, y, z);
    }

    public static ICropData getCropAtLocation(World world, int x, int y, int z) {
        if (!world.isRemote) {
            return getServerTracker().getCropDataForLocation(world, x, y, z);
        } else return getClientTracker().getCropDataForLocation(world, x, y, z);
    }

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

    public static boolean isHydrated(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) instanceof BlockFarmland && world.getBlockMetadata(x, y, z) == 7;
    }

    public static boolean plantCrop(EntityPlayer player, World world, int x, int y, int z, Crop crop, int quality) {
        return getTracker(world).plantCrop(player, world, x, y, z, crop, quality, 1);
    }

    public static void plantCrop(Object object, World world, int x, int y, int z, ICrop crop, int quality, int regrowStage) {
        // TODO Auto-generated method stub

    }

    public static boolean harvestCrop(EntityPlayer player, World world, int x, int y, int z) {
        ItemStack stack = getTracker(world).harvest(player, world, x, y, z);
        if (!world.isRemote && stack != null) {
            ItemHelper.dropBlockAsItem(world, x, y, z, stack);
        }

        return stack != null;
    }

    public static boolean canBonemeal(World world, int x, int y, int z) {
        if (!world.isRemote) {
            return getServerTracker().canBonemeal(world, x, y, z);
        } else return ClientHelper.getCropTracker().canBonemeal(world, x, y, z);
    }

    public static Crop getCropFromDamage(int damage) {
        int ordinal = getCropType(damage);
        return getCropFromOrdinal(ordinal);
    }

    /** Returns a crop based on it's ordinal **/
    public static Crop getCropFromOrdinal(int ordinal) {
        return HMConfiguration.mappings.getCrop(ordinal);
    }

    /** @return returns the Quality of this crop **/
    public static int getCropQuality(int meta) {
        return 1 + (int) Math.ceil(meta / 100);
    }

    /** @return Returns the CropMeta for this crop **/
    private static int getCropType(int meta) {
        return meta % 100;
    }

    public static void onHarvested(EntityPlayer player, CropData data) {
        ServerHelper.getPlayerData(player).onHarvested(data);
    }

    public static CropTrackerCommon getTracker(World world) {
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
