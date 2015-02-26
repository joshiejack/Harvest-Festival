package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.core.helpers.generic.ItemHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.crops.CropData;
import joshie.harvestmoon.crops.CropTrackerClient;
import joshie.harvestmoon.crops.CropTrackerServer;
import joshie.harvestmoon.init.HMConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CropHelper {
    public static boolean destroyCrop(EntityPlayer player, World world, int x, int y, int z) {
        if (!world.isRemote) {
            return getServerTracker().destroy(player, world, x, y, z);
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getIconForCrop(World world, int x, int y, int z) {
        return getClientTracker().getIconForCrop(world, x, y, z);
    }

    public static boolean hydrate(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        boolean ret = meta == 7 ? false : world.setBlockMetadataWithNotify(x, y, z, 7, 2);
        if (ret) {
            if (!world.isRemote) {
                getServerTracker().hydrate(world, x, y + 1, z);
            }
        }

        return ret;
    }

    //Returns false if the soil is no longer farmland
    public static boolean dehydrate(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y + 1, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == HMModInfo.FARMLAND_MINE_HOED_META) {
            return true;
        } else if (block instanceof IPlantable && world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable) block)) {
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
        if (!world.isRemote) {
            return getServerTracker().plant(player, world, x, y, z, crop, quality);
        }

        return true;
    }

    public static boolean harvestCrop(EntityPlayer player, World world, int x, int y, int z) {
        ItemStack stack = null;
        if (world.isRemote) {
            stack = ClientHelper.getCropTracker().harvest(world, x, y, z);
        } else {
            stack = getServerTracker().harvest(player, world, x, y, z);
        }

        if (stack != null) {
            ItemHelper.spawnItem(world, x, y, z, stack);
        }

        return stack != null;
    }

    public static void addFarmland(World world, int x, int y, int z) {
        if (!world.isRemote) {
            world.setBlock(x, y, z, Blocks.farmland);
            getServerTracker().addFarmland(world, x, y, z);
        }
    }

    /** DO NOT RENAME OR DELETE, CALL VIA ASM */
    public static void removeFarmland(World world, int x, int y, int z) {
        if (!world.isRemote) {
            getServerTracker().removeFarmland(world, x, y, z);
        }
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
