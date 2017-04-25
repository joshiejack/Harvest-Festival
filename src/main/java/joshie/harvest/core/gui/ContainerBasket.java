package joshie.harvest.core.gui;

import com.google.common.base.Optional;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.base.gui.ContainerBase;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.entity.EntityBasket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static joshie.harvest.core.entity.EntityBasket.ITEM;
import static joshie.harvest.core.tile.TileBasket.BASKET_INVENTORY;

public class ContainerBasket extends ContainerBase {
    private final ItemStackHandler handler;
    private final ItemStack basketItem;

    public ContainerBasket(InventoryPlayer inventory, @Nonnull ItemStack basketItem, EntityBasket basketEntity) {
        this.handler = !basketItem.isEmpty() ? new ItemStackHandler(BASKET_INVENTORY) : basketEntity.handler;
        this.basketItem = basketItem;
        if (!basketItem.isEmpty() && basketItem.getTagCompound() != null) {
            this.handler.deserializeNBT(basketItem.getTagCompound().getCompoundTag("inventory"));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new BasketSlotItemHandler(basketEntity, handler, j + i * 9, 8 + j * 18, (i * 18) + 18));
            }
        }

        bindPlayerInventory(inventory, 2);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!basketItem.isEmpty()) {
            NBTTagCompound tag = basketItem.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
                basketItem.setTagCompound(tag);
            }

            tag.setTag("inventory", handler.serializeNBT());
        }
    }

    @Override
    protected int getInventorySize() {
        return handler.getSlots();
    }

    @Override
    protected boolean isValid(@Nonnull ItemStack stack) {
        return HFApi.shipping.getSellValue(stack) > 0;
    }

    @Override
    protected Slot createSlot(InventoryPlayer inventory, int id, int x, int y) {
        return new Slot(inventory, id, x, y) {
            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return getStack().getItem() != Item.getItemFromBlock(HFCore.STORAGE) || HFCore.STORAGE.getEnumFromStack(getStack()) != Storage.BASKET;
            }
        };
    }

    private class BasketSlotItemHandler extends SlotItemHandler {
        private final EntityBasket basketEntity;

        BasketSlotItemHandler(EntityBasket basket, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.basketEntity = basket;
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();
            if (basketEntity != null && !getStack().isEmpty()) {
                basketEntity.getDataManager().set(ITEM, getStack());
            }
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return HFApi.shipping.getSellValue(stack) > 0;
        }
    }
}