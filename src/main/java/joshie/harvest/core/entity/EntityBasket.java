package joshie.harvest.core.entity;

import com.google.common.base.Optional;
import joshie.harvest.api.HFApi;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

import static joshie.harvest.core.tile.TileBasket.BASKET_INVENTORY;

public class EntityBasket extends Entity {
    public static final DataParameter<Optional<ItemStack>> ITEM = EntityDataManager.createKey(EntityItem.class, DataSerializers.OPTIONAL_ITEM_STACK);
    public final ItemStackHandler handler = new ItemStackHandler(BASKET_INVENTORY);

    public EntityBasket(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(ITEM, Optional.absent());
    }

    public void setAppearanceAndContents(@Nullable ItemStack stack, ItemStackHandler handler) {
        this.getDataManager().set(ITEM, Optional.fromNullable(stack));
        this.getDataManager().setDirty(ITEM);
        for (int i = 0; i < handler.getSlots(); i++) {
            this.handler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStack getEntityItem()  {
        ItemStack itemstack = (ItemStack)((Optional)this.getDataManager().get(ITEM)).orNull();
        return itemstack == null ?   new ItemStack(Blocks.STONE): itemstack;
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        handler.deserializeNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setTag("inventory", handler.serializeNBT());
    }

    /* Autoshipping some items **/
    private void autoship(List<ItemStack> list) {
        Iterator<ItemStack> it = list.iterator();
        while (it.hasNext()) {
            ItemStack stack = it.next();
            if (HFApi.shipping.getSellValue(stack) > 0) {
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(handler, stack, false);
                if (remainder == null) it.remove();
                else {
                    stack.stackSize = remainder.stackSize; //Update the internal contents
                }
            }
        }
    }

    public static boolean findBasketAndShip(EntityPlayer player, List<ItemStack> list) {
        for (Entity entity : player.getPassengers()) {
            if (entity instanceof EntityBasket) {
                EntityBasket basket = (EntityBasket) entity;
                if (list.size() > 0) {
                    basket.getDataManager().set(ITEM, Optional.of(list.get(list.size() - 1).copy()));
                    basket.getDataManager().setDirty(ITEM);
                }

                basket.autoship(list);
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
