package joshie.harvest.core.entity;

import com.google.common.base.Optional;
import joshie.harvest.core.block.BlockStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class EntityBasket extends Entity {
    private static final DataParameter<Optional<ItemStack>> ITEM = EntityDataManager.<Optional<ItemStack>>createKey(EntityItem.class, DataSerializers.OPTIONAL_ITEM_STACK);

    public EntityBasket(World worldIn) {
        super(worldIn);
        setEntityItemStack(new ItemStack(Blocks.AIR, 0));
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(ITEM, Optional.<ItemStack>absent());
    }

    public void setEntityItemStack(@Nullable ItemStack stack) {
        this.getDataManager().set(ITEM, Optional.fromNullable(stack));
        this.getDataManager().setDirty(ITEM);
    }

    public ItemStack getEntityItem()  {
        ItemStack itemstack = (ItemStack)((Optional)this.getDataManager().get(ITEM)).orNull();

        if (itemstack == null) {
            return new ItemStack(Blocks.STONE);
        } else {
            return itemstack;
        }
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) { }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {}

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
