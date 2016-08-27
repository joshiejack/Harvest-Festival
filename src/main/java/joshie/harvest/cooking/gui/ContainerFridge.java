package joshie.harvest.cooking.gui;

import joshie.harvest.cooking.blocks.FridgeData;
import joshie.harvest.cooking.blocks.TileFridge;
import joshie.harvest.core.util.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFridge extends ContainerBase {
    private final TileFridge fridge;

    public ContainerFridge(EntityPlayer player, IInventory inventory, TileFridge fridge) {
        this.fridge = fridge;
        fridge.getContents().openInventory(player);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotFridge(fridge.getContents(), j + i * 9, 8 + j * 18, (i * 18) + 18));
            }
        }

        bindPlayerInventory(inventory, 56);
    }

    private void bindPlayerInventory(IInventory playerInventory, int yOffset) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yOffset));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142 + yOffset));
        }
    }

    @Override
    public int getMaximumStorage(int size) {
        return size * 8;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return fridge.getContents().isUseableByPlayer(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        fridge.getContents().closeInventory(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        int size = fridge.getContents().getSizeInventory();
        int low = size + 27;
        int high = low + 9;
        ItemStack newStack = null;
        final Slot slot = inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            newStack = stack.copy();

            if (slotID < size) {
                if (!mergeItemStack(stack, size, high, true)) return null;
            } else if (TileFridge.isValid(stack)) {
                if (!mergeItemStack(stack, 0, fridge.getContents().getSizeInventory(), false)) return null;
            } else if (slotID >= size && slotID < low) {
                if (!mergeItemStack(stack, low, high, false)) return null;
            } else if (slotID >= low && slotID < high && !mergeItemStack(stack, size, low, false)) return null;

            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (stack.stackSize == newStack.stackSize) return null;

            slot.onPickupFromSlot(player, stack);
        }

        return newStack;
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        detectAndSendChanges();
    }

    private class SlotFridge extends SlotHF {
        public SlotFridge(FridgeData invent, int slot, int x, int y) {
            super(invent, slot, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return TileFridge.isValid(stack);
        }
    }
}