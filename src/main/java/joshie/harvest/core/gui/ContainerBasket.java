package joshie.harvest.core.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.base.gui.ContainerBase;
import joshie.harvest.core.block.BlockStorage.Storage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import static joshie.harvest.core.tile.TileBasket.BASKET_INVENTORY;

public class ContainerBasket extends ContainerBase {
    private final ItemStackHandler handler;
    private final ItemStack basket;

    public ContainerBasket(InventoryPlayer inventory, ItemStack basket) {
        this.handler = new ItemStackHandler(BASKET_INVENTORY);
        this.basket = basket;
        if (basket.getTagCompound() != null) {
            this.handler.deserializeNBT(basket.getTagCompound().getCompoundTag("inventory"));
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(handler, j + i * 9, 8 + j * 18, (i * 18) + 18) {
                    @Override
                    public boolean isItemValid(ItemStack stack) {
                        return isValid(stack);
                    }
                });
            }
        }

        bindPlayerInventory(inventory, 56);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        NBTTagCompound tag = basket.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            basket.setTagCompound(tag);
        }

        tag.setTag("inventory", handler.serializeNBT());
    }

    @Override
    protected int getInventorySize() {
        return handler.getSlots();
    }

    @Override
    protected boolean isValid(ItemStack stack) {
        return HFApi.shipping.getSellValue(stack) > 0;
    }

    @Override
    protected Slot createSlot(InventoryPlayer inventory, int id, int x, int y) {
        return new Slot(inventory, id, x, y) {
            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return getStack() == null || getStack().getItem() != Item.getItemFromBlock(HFCore.STORAGE) || HFCore.STORAGE.getEnumFromStack(getStack()) != Storage.BASKET;
            }
        };
    }
}