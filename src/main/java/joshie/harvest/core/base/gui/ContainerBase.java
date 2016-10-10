package joshie.harvest.core.base.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

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
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yOffset));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142 + yOffset));
        }
    }

    public static class SlotHF extends Slot {
        public SlotHF(IInventory invent, int slot, int x, int y) {
            super(invent, slot, x, y);
        }
    }

    @Override
    public ItemStack slotClick(int slotID, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        Slot slot = slotID < 0 || slotID > inventorySlots.size() ? null : inventorySlots.get(slotID);
        return slot instanceof SlotHF ? specialClick(slotID, dragType, clickTypeIn, player) : super.slotClick(slotID, dragType, clickTypeIn, player);
    }

    public int getMaximumStorage(int size) {
        return size;
    }

    private int getMaxStackSize(ItemStack stack, boolean reverse) {
        if (reverse) return getMaximumStorage(stack.getMaxStackSize());
        return stack.getMaxStackSize();
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean movingIn = startIndex == 0;
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                Slot slot = inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();

                if (itemstack != null && areItemStacksEqual(stack, itemstack)) {
                    int j = itemstack.stackSize + stack.stackSize;
                    if (j <= getMaxStackSize(stack, movingIn)) {
                        stack.stackSize = 0;
                        itemstack.stackSize = j;
                        slot.onSlotChanged();
                        flag = true;
                    } else if (itemstack.stackSize < getMaxStackSize(stack, movingIn)) {
                        stack.stackSize -= getMaxStackSize(stack, movingIn) - itemstack.stackSize;
                        itemstack.stackSize = getMaxStackSize(stack, movingIn);
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (stack.stackSize > 0) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex) {
                Slot slot1 = inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1 == null && slot1.isItemValid(stack)) {
                    ItemStack clone = stack.copy();
                    if (stack.stackSize >= stack.getMaxStackSize()) {
                        clone.stackSize = stack.getMaxStackSize();
                    }

                    slot1.putStack(clone);
                    slot1.onSlotChanged();
                    stack.stackSize = stack.stackSize - clone.stackSize;
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return stackB.getItem() == stackA.getItem() && (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) && ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

    //From vanilla
    @Nullable
    public ItemStack specialClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int i = dragEvent;
            dragEvent = getDragEvent(dragType);

            if ((i != 1 || dragEvent != 2) && i != dragEvent) {
                resetDrag();
            } else if (inventoryplayer.getItemStack() == null) {
                resetDrag();
            } else if (dragEvent == 0) {
                dragMode = extractDragMode(dragType);

                if (isValidDragMode(dragMode, player)) {
                    dragEvent = 1;
                    dragSlots.clear();
                } else {
                    resetDrag();
                }
            } else if (dragEvent == 1) {
                Slot slot = inventorySlots.get(slotId);

                if (slot != null && canAddItemToSlot(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > dragSlots.size() && canDragIntoSlot(slot)) {
                    dragSlots.add(slot);
                }
            } else if (dragEvent == 2) {
                if (!dragSlots.isEmpty()) {
                    ItemStack itemstack3 = inventoryplayer.getItemStack().copy();
                    int j = inventoryplayer.getItemStack().stackSize;

                    for (Slot slot1 : dragSlots) {
                        if (slot1 != null && canAddItemToSlot(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= dragSlots.size() && canDragIntoSlot(slot1)) {
                            ItemStack itemstack1 = itemstack3.copy();
                            int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                            computeStackSize(dragSlots, dragMode, itemstack1, k);

                            if (itemstack1.stackSize > getMaxStackSize(itemstack1, true)) {
                                itemstack1.stackSize = getMaxStackSize(itemstack1, true);
                            }

                            if (itemstack1.stackSize > slot1.getItemStackLimit(itemstack1)) {
                                itemstack1.stackSize = slot1.getItemStackLimit(itemstack1);
                            }

                            j -= itemstack1.stackSize - k;
                            slot1.putStack(itemstack1);
                        }
                    }

                    itemstack3.stackSize = j;

                    if (itemstack3.stackSize <= 0) {
                        itemstack3 = null;
                    }

                    inventoryplayer.setItemStack(itemstack3);
                }

                resetDrag();
            } else {
                resetDrag();
            }
        } else if (dragEvent != 0) {
            resetDrag();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (inventoryplayer.getItemStack() != null) {
                    if (dragType == 0) {
                        player.dropItem(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack(null);
                    }

                    if (dragType == 1) {
                        player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);

                        if (inventoryplayer.getItemStack().stackSize == 0) {
                            inventoryplayer.setItemStack(null);
                        }
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return null;
                }

                Slot slot6 = inventorySlots.get(slotId);

                if (slot6 != null && slot6.canTakeStack(player)) {
                    ItemStack itemstack8 = slot6.getStack();

                    if (itemstack8 != null && itemstack8.stackSize <= 0) {
                        itemstack = itemstack8.copy();
                        slot6.putStack(null);
                    }

                    ItemStack itemstack11 = transferStackInSlot(player, slotId);

                    if (itemstack11 != null) {
                        Item item = itemstack11.getItem();
                        itemstack = itemstack11.copy();

                        if (slot6.getStack() != null && slot6.getStack().getItem() == item) {
                            retrySlotClick(slotId, dragType, true, player);
                        }
                    }
                }
            } else {
                if (slotId < 0) {
                    return null;
                }

                Slot slot7 = inventorySlots.get(slotId);

                if (slot7 != null) {
                    ItemStack inSlot = slot7.getStack();
                    ItemStack held = inventoryplayer.getItemStack();

                    if (inSlot != null) {
                        itemstack = inSlot.copy();
                    }

                    if (inSlot == null) {
                        if (held != null && slot7.isItemValid(held)) {
                            int l2 = dragType == 0 ? held.stackSize : 1;

                            if (l2 > slot7.getItemStackLimit(held)) {
                                l2 = slot7.getItemStackLimit(held);
                            }

                            slot7.putStack(held.splitStack(l2));

                            if (held.stackSize == 0) {
                                inventoryplayer.setItemStack(null);
                            }
                        }
                    } else if (slot7.canTakeStack(player)) {
                        if (held == null) {
                            if (inSlot.stackSize > 0) {
                                int k2 = dragType == 0 ? inSlot.stackSize : (inSlot.stackSize + 1) / 2;
                                if (k2 > getMaxStackSize(inSlot, true)) {
                                    k2 = getMaxStackSize(inSlot, true);
                                }

                                inventoryplayer.setItemStack(slot7.decrStackSize(k2));

                                if (inSlot.stackSize <= 0) {
                                    slot7.putStack(null);
                                }

                                slot7.onPickupFromSlot(player, inventoryplayer.getItemStack());
                            } else {
                                slot7.putStack(null);
                                inventoryplayer.setItemStack(null);
                            }
                        } else if (slot7.isItemValid(held)) {
                            if (inSlot.getItem() == held.getItem() && inSlot.getMetadata() == held.getMetadata() && ItemStack.areItemStackTagsEqual(inSlot, held)) {
                                int j2 = dragType == 0 ? held.stackSize : 1;

                                if (j2 > slot7.getItemStackLimit(held) - inSlot.stackSize) {
                                    j2 = slot7.getItemStackLimit(held) - inSlot.stackSize;
                                }

                                if (j2 > getMaxStackSize(held, true) - inSlot.stackSize) {
                                    j2 = getMaxStackSize(held, true) - inSlot.stackSize;
                                }

                                held.splitStack(j2);

                                if (held.stackSize == 0) {
                                    inventoryplayer.setItemStack(null);
                                }

                                inSlot.stackSize += j2;
                            } else if (held.stackSize <= slot7.getItemStackLimit(held)) {
                                slot7.putStack(held);
                                inventoryplayer.setItemStack(inSlot);
                            }
                        } else if (inSlot.getItem() == held.getItem() && held.getMaxStackSize() > 1 && (!inSlot.getHasSubtypes() || inSlot.getMetadata() == held.getMetadata()) && ItemStack.areItemStackTagsEqual(inSlot, held)) {
                            int i2 = inSlot.stackSize;

                            if (i2 > 0 && i2 + held.stackSize <= held.getMaxStackSize()) {
                                held.stackSize += i2;
                                inSlot = slot7.decrStackSize(i2);

                                if (inSlot.stackSize == 0) {
                                    slot7.putStack(null);
                                }

                                slot7.onPickupFromSlot(player, inventoryplayer.getItemStack());
                            }
                        }
                    }

                    slot7.onSlotChanged();
                }
            }
        } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            Slot slot5 = inventorySlots.get(slotId);
            ItemStack itemstack7 = inventoryplayer.getStackInSlot(dragType);

            if (itemstack7 != null && itemstack7.stackSize <= 0) {
                itemstack7 = null;
                inventoryplayer.setInventorySlotContents(dragType, null);
            }

            ItemStack itemstack10 = slot5.getStack();
            if (itemstack7 != null || itemstack10 != null) {
                if (itemstack7 == null) {
                    if (slot5.canTakeStack(player)) {
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        slot5.putStack(null);
                        slot5.onPickupFromSlot(player, itemstack10);
                    }
                } else if (itemstack10 == null) {
                    if (slot5.isItemValid(itemstack7)) {
                        int k1 = slot5.getItemStackLimit(itemstack7);

                        if (itemstack7.stackSize > k1) {
                            slot5.putStack(itemstack7.splitStack(k1));
                        } else {
                            slot5.putStack(itemstack7);
                            inventoryplayer.setInventorySlotContents(dragType, null);
                        }
                    }
                } else if (slot5.canTakeStack(player) && slot5.isItemValid(itemstack7)) {
                    int l1 = slot5.getItemStackLimit(itemstack7);

                    if (itemstack7.stackSize > l1) {
                        slot5.putStack(itemstack7.splitStack(l1));
                        slot5.onPickupFromSlot(player, itemstack10);

                        if (!inventoryplayer.addItemStackToInventory(itemstack10)) {
                            player.dropItem(itemstack10, true);
                        }
                    } else {
                        slot5.putStack(itemstack7);
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        slot5.onPickupFromSlot(player, itemstack10);
                    }
                }
            }
        } else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0) {
            Slot slot4 = inventorySlots.get(slotId);

            if (slot4 != null && slot4.getHasStack()) {
                if (slot4.getStack().stackSize > 0) {
                    ItemStack itemstack6 = slot4.getStack().copy();
                    itemstack6.stackSize = itemstack6.getMaxStackSize();
                    inventoryplayer.setItemStack(itemstack6);
                } else {
                    slot4.putStack(null);
                }
            }
        } else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack() == null && slotId >= 0) {
            Slot slot3 = inventorySlots.get(slotId);

            if (slot3 != null && slot3.getHasStack() && slot3.canTakeStack(player)) {
                ItemStack itemstack5 = slot3.decrStackSize(dragType == 0 ? 1 : slot3.getStack().stackSize);
                slot3.onPickupFromSlot(player, itemstack5);
                player.dropItem(itemstack5, true);
            }
        } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot2 = inventorySlots.get(slotId);
            ItemStack itemstack4 = inventoryplayer.getItemStack();

            if (itemstack4 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player))) {
                int i1 = dragType == 0 ? 0 : inventorySlots.size() - 1;
                int j1 = dragType == 0 ? 1 : -1;

                for (int i3 = 0; i3 < 2; ++i3) {
                    for (int j3 = i1; j3 >= 0 && j3 < inventorySlots.size() && itemstack4.stackSize < itemstack4.getMaxStackSize(); j3 += j1) {
                        Slot slot8 = inventorySlots.get(j3);

                        if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack4, true) && slot8.canTakeStack(player) && canMergeSlot(itemstack4, slot8) && (i3 != 0 || slot8.getStack().stackSize != slot8.getStack().getMaxStackSize())) {
                            int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize, slot8.getStack().stackSize);
                            ItemStack itemstack2 = slot8.decrStackSize(l);
                            itemstack4.stackSize += l;

                            if (itemstack2.stackSize <= 0) {
                                slot8.putStack(null);
                            }

                            slot8.onPickupFromSlot(player, itemstack2);
                        }
                    }
                }
            }

            detectAndSendChanges();
        }

        return itemstack;
    }
}
