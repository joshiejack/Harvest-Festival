package joshie.harvest.core.helpers;

import joshie.harvest.calendar.HFCalendar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class SpawnItemHelper {
    public static void addToPlayerInventory(EntityPlayer player, ItemStack stack) {
        ItemHandlerHelper.giveItemToPlayer(player, stack.copy());
    }
    
    //Items Spawned by entities last 1 day
    public static void spawnByEntity(Entity entity, ItemStack stack) {
        spawnItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, stack, false, (int) HFCalendar.TICKS_PER_DAY, 0, 0);
    }

    public static void spawnXP(World world, int x, int y, int z, int amount) {
        if (!world.isRemote) {
            EntityXPOrb orb = new EntityXPOrb(world, x, y, z, amount);
            world.spawnEntityInWorld(orb);
        }
    }
    
    private static ItemStack spawnItem(World world, double x, double y, double z, ItemStack stack, boolean random, int lifespan, int delay, double motion) {
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

            entityitem.setPickupDelay(delay);
            world.spawnEntityInWorld(entityitem);
        }

        return null;
    }
    
    public static void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack) {
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops") && !world.restoringBlockSnapshots) {
            float f = 0.7F;
            double d0 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double) x + d0, (double) y + d1, (double) z + d2, stack);
            entityitem.setPickupDelay(10);
            world.spawnEntityInWorld(entityitem);
        }
    }
}
