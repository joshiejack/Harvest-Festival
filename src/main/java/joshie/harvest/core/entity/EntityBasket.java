package joshie.harvest.core.entity;

import joshie.harvest.core.block.BlockStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class EntityBasket extends Entity {
    public EntityBasket(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {}

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {}

    /* Autoshipping some items **/
    private void autoship(World world, EntityPlayer player, List<ItemStack> list) {
        Iterator<ItemStack> it = list.iterator();
        while (it.hasNext()) {
            ItemStack stack = it.next();
            if (BlockStorage.hasShippedItem(world, player, stack)) {
                it.remove();
            }
        }
    }

    public static boolean findBasketAndShip(EntityPlayer player, List<ItemStack> list) {
        for (Entity entity : player.getPassengers()) {
            if (entity instanceof EntityBasket) {
                ((EntityBasket)entity).autoship(player.worldObj, player, list);
                return list.isEmpty();
            }
        }

        return false;
    }

    public static boolean isWearingBasket(EntityPlayer player) {
        for (Entity entity : player.getPassengers()) {
            if (entity instanceof EntityBasket) {
                return true;
            }
        }

        return false;
    }
}
