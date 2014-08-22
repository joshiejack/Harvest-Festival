package harvestmoon.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHelper {
    public static void addToPlayerInventory(EntityPlayer player, ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack)) {
            if (!player.worldObj.isRemote) {
                spawnItem(player.worldObj, player.posX, player.posY, player.posZ, stack, false, 0, 0, 0);
            }
        }
    }
    
    //Items Spawned by entities last 24 hours
    public static void spawnByEntity(Entity entity, ItemStack stack) {
        spawnItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, stack, false, 24000, 0, 0);
    }
    
    public static ItemStack spawnItem(World world, double x, double y, double z, ItemStack stack, boolean random, int lifespan, int delay, double motion) {
        if (!world.isRemote) {
            float f = 0.7F;
            double d0 = random ? world.rand.nextFloat() * f + (1.0F - f) * 0.5D : 0.5D;
            double d1 = random ? world.rand.nextFloat() * f + (1.0F - f) * 0.5D : 0.5D;
            double d2 = random ? world.rand.nextFloat() * f + (1.0F - f) * 0.5D : 0.5D;
            EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, stack);
            if (motion > 0D) {
                entityitem.motionX = world.rand.nextFloat() * f * motion;
                entityitem.motionY = world.rand.nextFloat() * f * motion;
                entityitem.motionZ = world.rand.nextFloat() * f * motion;
            }

            if (lifespan > 0) {
                entityitem.lifespan = lifespan;
            }

            entityitem.delayBeforeCanPickup = delay;
            world.spawnEntityInWorld(entityitem);
        }

        return null;
    }
}
