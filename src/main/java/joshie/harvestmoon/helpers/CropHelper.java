package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.init.HMBlocks;
import joshie.lib.helpers.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CropHelper {
    public static boolean destroyCrop(EntityPlayer player, World world, int x, int y, int z) {
        if(!world.isRemote) {
            return handler.getServer().getCropTracker().destroy(player, world, x, y, z);
        }
        
        return true;
    }
    
    public static void hydrate(World world, int x, int y, int z) {
        if(!world.isRemote) {
            handler.getServer().getCropTracker().hydrate(world, x, y, z);
        }
    }
    
    public static boolean plantCrop(EntityPlayer player, World world, int x, int y, int z, Crop crop, int quality) {
        if(!world.isRemote) {
            return handler.getServer().getCropTracker().plant(player, world, x, y, z, crop, quality);
        }
        
        return true;
    }
    
    public static boolean harvestCrop(EntityPlayer player, World world, int x, int y, int z) {
        ItemStack stack = null;
        if(world.isRemote) {
            stack = handler.getClient().getCropTracker().harvest(world, x, y, z);
        } else {
            stack = handler.getServer().getCropTracker().harvest(player, world, x, y, z);
        }
        
        if(stack != null) {
            ItemHelper.addToPlayerInventory(player, stack);
        }
        
        return stack != null;
    }
    
    public static void addFarmland(World world, int x, int y, int z) {
        if (!world.isRemote) {
            world.setBlock(x, y, z, HMBlocks.soil);
            handler.getServer().getCropTracker().addFarmland(world, x, y, z);
        }
    }
    
    public static void removeFarmland(World world, int x, int y, int z) {
        if (!world.isRemote) {
            handler.getServer().getCropTracker().removeFarmland(world, x, y, z);
        }
    }

    /** Returns the crop based on it's stack **/
    public static Crop getCropFromStack(ItemStack stack) {
        int meta = getCropType(stack.getItemDamage());
        for (Crop crop : Crop.crops) {
            if (crop.getCropMeta() == meta) {
                return crop;
            }
        }

        return null;
    }

    /** Returns a crop based on it's ordinal **/
    public static Crop getCropFromOrdinal(int ordinal) {
        for (Crop crop : Crop.crops) {
            if (crop.getCropMeta() == ordinal) {
                return crop;
            }
        }

        return null;
    }

    /** @return returns the Quality of this crop **/
    public static int getCropQuality(int meta) {
        return (int) Math.ceil(getInternalMeta(meta) / 100);
    }

    /** @return Returns the CropMeta for this crop **/
    public static int getCropType(int meta) {
        return getInternalMeta(meta) % 100;
    }

    /** @return Returns a value of metadata, removing the the size data **/
    private static int getInternalMeta(int meta) {
        return meta % 16000;
    }

    /** Whether this crop is a giant crop or small **/
    public static boolean isGiant(int meta) {
        return ((int) (meta / 16000)) == 1;
    }
}
