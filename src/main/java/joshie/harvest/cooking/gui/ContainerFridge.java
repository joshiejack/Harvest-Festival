package joshie.harvest.cooking.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.player.fridge.FridgeData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ContainerFridge extends ContainerBase {
    private final FridgeData storage;

    public ContainerFridge(IInventory inventory, FridgeData storage) {
        this.storage = storage;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(storage, j + i * 9, 8 + j * 18, (i * 18) + 18));
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
    public boolean canInteractWith(EntityPlayer player) {
        return storage.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        int size = storage.getSizeInventory();
        int low = size + 27;
        int high = low + 9;
        ItemStack newStack = null;
        final Slot slot = inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            newStack = stack.copy();

            if (slotID < size) {
                if (!mergeItemStack(stack, size, high, true)) return null;
            } else if (stack.getItem() == HFCooking.MEAL || HFApi.cooking.getCookingComponents(stack).size() > 0 || stack.getItem() instanceof ItemFood) {
                if (!mergeItemStack(stack, 0, storage.getSizeInventory(), false)) return null;
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
}