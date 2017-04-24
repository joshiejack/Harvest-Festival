package joshie.harvest.core.base.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {
    public ContainerBase() {}

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    protected void bindPlayerInventory(InventoryPlayer inventory) {
        bindPlayerInventory(inventory, 0);
    }

    protected void bindPlayerInventory(InventoryPlayer inventory, int yOffset) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(createSlot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yOffset));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(createSlot(inventory, i, 8 + i * 18, 142 + yOffset));
        }
    }

    protected Slot createSlot(InventoryPlayer inventory, int id, int x, int y) {
        return new Slot(inventory, id, x, y);
    }

    protected boolean isValid(ItemStack stack) {
        return true;
    }

    protected int getInventorySize() {
        return 0;
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        detectAndSendChanges();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        int size = getInventorySize();
        int low = size + 27;
        int high = low + 9;
        ItemStack newStack = null;
        final Slot slot = inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            newStack = stack.copy();
            if (slotID < size) {
                if (!mergeItemStack(stack, size, high, true)) return null;
            } else if (isValid(stack)) {
                if (!mergeItemStack(stack, 0, size, false)) return null;
            } else if (slotID >= size && slotID < low) {
                if (!mergeItemStack(stack, low, high, false)) return null;
            } else if (slotID >= low && slotID < high && !mergeItemStack(stack, size, low, false)) return null;

            if (stack.getCount() == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == newStack.getCount()) return null;

            slot.onTake(player, stack);
        }

        return newStack;
    }
}
