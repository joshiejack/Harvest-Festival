package joshie.harvest.core.helpers;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.ItemHelper;
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
            HFTrackers.getCropTracker().hydrate(world, x, y + 1, z);
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

    //Harvests the crop at this location
    public static boolean harvestCrop(EntityPlayer player, World world, int x, int y, int z) {
        ItemStack stack = HFTrackers.getCropTracker().harvest(player, world, x, y, z);
        if (!world.isRemote && stack != null) {
            ItemHelper.dropBlockAsItem(world, x, y, z, stack);
        }

        return stack != null;
    }
}
