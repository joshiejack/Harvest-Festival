package joshie.harvest.cooking.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.player.fridge.FridgeData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class ContainerFridge extends ContainerBase {
    private final FridgeData storage;
    private final Random rand = new Random();

    public ContainerFridge(IInventory inventory, FridgeData storage) {
        this.storage = storage;
        addSlotToContainer(new Slot(storage, 0, 1, 1));
        bindPlayerInventory(inventory, 0);
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
    public boolean canInteractWith(EntityPlayer player) {
        return storage.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        int size = storage.getSizeInventory();
        int low = size + 27;
        int high = low + 9;
        ItemStack newStack = null;
        final Slot slot = (Slot) inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            newStack = stack.copy();

            if (slotID < size) {
                if (!mergeItemStack(stack, size, high, true)) return null;
            } else if (HFApi.COOKING.getCookingComponents(stack).size() > 0) {
                if (!mergeItemStack(stack, 0, storage.getSizeInventory(), false)) return null;
            } else if (slotID >= size && slotID < low) {
                if (!mergeItemStack(stack, low, high, false)) return null;
            } else if (slotID >= low && slotID < high && !mergeItemStack(stack, size, low, false)) return null;

            if (stack.stackSize == 0) {
                slot.putStack((ItemStack) null);
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
}
