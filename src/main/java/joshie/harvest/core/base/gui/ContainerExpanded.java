package joshie.harvest.core.base.gui;

import joshie.harvest.cooking.packet.PacketExpandedSlot;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerExpanded extends ContainerBase {
    public ContainerExpanded() {
    }

    public static class SlotHF extends Slot {
        public SlotHF(IInventory invent, int slot, int x, int y) {
            super(invent, slot, x, y);
        }
    }

    @Override
    public void detectAndSendChanges() {
        for (int i = 0; i < inventorySlots.size(); ++i) {
            ItemStack stack = (inventorySlots.get(i)).getStack();
            ItemStack stack1 = inventoryItemStacks.get(i);
            if (!ItemStack.areItemStacksEqual(stack1, stack)) {
                stack1 = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
                inventoryItemStacks.set(i, stack1);

                for (IContainerListener listener : listeners) {
                    if (listener instanceof EntityPlayerMP) {
                        if (!((EntityPlayerMP) listener).isChangingQuantityOnly) {
                            PacketHandler.sendToClient(new PacketExpandedSlot(windowId, i, stack1), (EntityPlayerMP) listener);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack slotClick(int slotID, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        Slot slot = slotID < 0 || slotID > inventorySlots.size() ? null : inventorySlots.get(slotID);
        return slot instanceof SlotHF ? specialClick(slotID, dragType, clickTypeIn, player) : super.slotClick(slotID, dragType, clickTypeIn, player);
    }

    public int getMaximumStorage(int size) {
        return size;
    }

    private int getMaxStackSize(@Nonnull ItemStack stack, boolean reverse) {
        if (reverse) return getMaximumStorage(stack.getMaxStackSize());
        return stack.getMaxStackSize();
    }

    @Override
    protected boolean mergeItemStack(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean movingIn = startIndex == 0;
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();

                if (areItemStacksEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = getMaxStackSize(stack, movingIn);

                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
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

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() >= slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
                    } else {
                        slot1.putStack(stack.splitStack(stack.getCount()));
                    }

                    slot1.onSlotChanged();
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

    private static boolean areItemStacksEqual(@Nonnull ItemStack stackA, @Nonnull ItemStack stackB) {
        return stackB.getItem() == stackA.getItem() && (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) && ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

    //From vanilla
    @Nonnull
    public ItemStack specialClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        ItemStack stack = ItemStack.EMPTY;
        InventoryPlayer inventory = player.inventory;

        if (clickType == ClickType.QUICK_CRAFT) {
            int i = dragEvent;
            dragEvent = getDragEvent(dragType);

            if ((i != 1 || dragEvent != 2) && i != dragEvent) {
                resetDrag();
            } else if (inventory.getItemStack().isEmpty()) {
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
                ItemStack itemstack1 = inventory.getItemStack();

                if (slot != null && canAddItemToSlot(slot, itemstack1, true) && slot.isItemValid(itemstack1) && (dragMode == 2 || itemstack1.getCount() > dragSlots.size()) && canDragIntoSlot(slot)) {
                    dragSlots.add(slot);
                }
            } else if (dragEvent == 2) {
                if (!dragSlots.isEmpty()) {
                    ItemStack itemstack5 = inventory.getItemStack().copy();
                    int l = inventory.getItemStack().getCount();

                    for (Slot slot1 : dragSlots) {
                        ItemStack itemstack2 = inventory.getItemStack();

                        if (slot1 != null && canAddItemToSlot(slot1, itemstack2, true) && slot1.isItemValid(itemstack2) && (dragMode == 2 || itemstack2.getCount() >= dragSlots.size()) && canDragIntoSlot(slot1)) {
                            ItemStack itemstack3 = itemstack5.copy();
                            int j = slot1.getHasStack() ? slot1.getStack().getCount() : 0;
                            computeStackSize(dragSlots, dragMode, itemstack3, j);
                            int k = getMaxStackSize(itemstack3, true);

                            if (itemstack3.getCount() > k) {
                                itemstack3.setCount(k);
                            }

                            l -= itemstack3.getCount() - j;
                            slot1.putStack(itemstack3);
                        }
                    }

                    itemstack5.setCount(l);
                    inventory.setItemStack(itemstack5);
                }

                resetDrag();
            } else {
                resetDrag();
            }
        } else if (dragEvent != 0) {
            resetDrag();
        } else if ((clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!inventory.getItemStack().isEmpty()) {
                    if (dragType == 0) {
                        player.dropItem(inventory.getItemStack(), true);
                        inventory.setItemStack(ItemStack.EMPTY);
                    }

                    if (dragType == 1) {
                        player.dropItem(inventory.getItemStack().splitStack(1), true);
                    }
                }
            } else if (clickType == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot6 = inventorySlots.get(slotId);

                if (slot6 != null && slot6.canTakeStack(player)) {
                    ItemStack itemstack10 = transferStackInSlot(player, slotId);

                    if (!itemstack10.isEmpty()) {
                        Item item = itemstack10.getItem();
                        stack = itemstack10.copy();

                        if (slot6.getStack().getItem() == item) {
                            retrySlotClick(slotId, dragType, true, player);
                        }
                    }
                }
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot7 = inventorySlots.get(slotId);

                if (slot7 != null) {
                    ItemStack inSlot = slot7.getStack();
                    ItemStack held = inventory.getItemStack();

                    if (!inSlot.isEmpty()) {
                        stack = inSlot.copy();
                    }

                    if (inSlot.isEmpty()) {
                        if (!held.isEmpty() && slot7.isItemValid(held)) {
                            int l2 = dragType == 0 ? held.getCount() : 1;

                            if (l2 > slot7.getItemStackLimit(held)) {
                                l2 = slot7.getItemStackLimit(held);
                            }

                            slot7.putStack(held.splitStack(l2));
                        }
                    } else if (slot7.canTakeStack(player)) {
                        if (held.isEmpty()) {
                            if (inSlot.isEmpty()) {
                                slot7.putStack(ItemStack.EMPTY);
                                inventory.setItemStack(ItemStack.EMPTY);
                            } else {
                                int k2 = dragType == 0 ? inSlot.getCount() : (inSlot.getCount() + 1) / 2;
                                int maxReverse = getMaxStackSize(inSlot, false);
                                if (k2 > maxReverse) {
                                    k2 = maxReverse;
                                }
                                inventory.setItemStack(slot7.decrStackSize(k2));

                                if (inSlot.isEmpty()) {
                                    slot7.putStack(ItemStack.EMPTY);
                                }

                                slot7.onTake(player, inventory.getItemStack());
                            }
                        } else if (slot7.isItemValid(held)) {
                            if (inSlot.getItem() == held.getItem() && inSlot.getMetadata() == held.getMetadata() && ItemStack.areItemStackTagsEqual(inSlot, held)) {
                                int j2 = dragType == 0 ? held.getCount() : 1;

                                if (j2 > slot7.getItemStackLimit(held) - inSlot.getCount()) {
                                    j2 = slot7.getItemStackLimit(held) - inSlot.getCount();
                                }

                                int max = getMaxStackSize(held, true);

                                if (j2 > max - inSlot.getCount()) {
                                    j2 = max - inSlot.getCount();
                                }

                                held.shrink(j2);
                                inSlot.grow(j2);
                            } else if (held.getCount() <= slot7.getItemStackLimit(held)) {
                                slot7.putStack(held);
                                inventory.setItemStack(inSlot);
                            }
                        } else if (inSlot.getItem() == held.getItem() && held.getMaxStackSize() > 1 && (!inSlot.getHasSubtypes() || inSlot.getMetadata() == held.getMetadata()) && ItemStack.areItemStackTagsEqual(inSlot, held) && !inSlot.isEmpty()) {
                            int i2 = inSlot.getCount();

                            if (i2 + held.getCount() <= held.getMaxStackSize()) {
                                held.grow(i2);
                                inSlot = slot7.decrStackSize(i2);

                                if (inSlot.isEmpty()) {
                                    slot7.putStack(ItemStack.EMPTY);
                                }

                                slot7.onTake(player, inventory.getItemStack());
                            }
                        }
                    }

                    slot7.onSlotChanged();
                }
            }
        } else if (clickType == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            Slot slot5 = inventorySlots.get(slotId);
            ItemStack itemstack9 = inventory.getStackInSlot(dragType);
            ItemStack itemstack12 = slot5.getStack();

            if (!itemstack9.isEmpty() || !itemstack12.isEmpty()) {
                if (itemstack9.isEmpty()) {
                    if (slot5.canTakeStack(player)) {
                        inventory.setInventorySlotContents(dragType, itemstack12);
                        //Removed onSwapCraft call, since it is protected
                        slot5.putStack(ItemStack.EMPTY);
                        slot5.onTake(player, itemstack12);
                    }
                } else if (itemstack12.isEmpty()) {
                    if (slot5.isItemValid(itemstack9)) {
                        int k1 = slot5.getItemStackLimit(itemstack9);

                        if (itemstack9.getCount() > k1) {
                            slot5.putStack(itemstack9.splitStack(k1));
                        } else {
                            slot5.putStack(itemstack9);
                            inventory.setInventorySlotContents(dragType, ItemStack.EMPTY);
                        }
                    }
                } else if (slot5.canTakeStack(player) && slot5.isItemValid(itemstack9)) {
                    int l1 = slot5.getItemStackLimit(itemstack9);

                    if (itemstack9.getCount() > l1) {
                        slot5.putStack(itemstack9.splitStack(l1));
                        slot5.onTake(player, itemstack12);

                        if (!inventory.addItemStackToInventory(itemstack12)) {
                            player.dropItem(itemstack12, true);
                        }
                    } else {
                        slot5.putStack(itemstack9);
                        inventory.setInventorySlotContents(dragType, itemstack12);
                        slot5.onTake(player, itemstack12);
                    }
                }
            }
        } else if (clickType == ClickType.CLONE && player.capabilities.isCreativeMode && inventory.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot4 = inventorySlots.get(slotId);

            if (slot4 != null && slot4.getHasStack()) {
                ItemStack itemstack8 = slot4.getStack().copy();
                itemstack8.setCount(itemstack8.getMaxStackSize());
                inventory.setItemStack(itemstack8);
            }
        } else if (clickType == ClickType.THROW && inventory.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot3 = inventorySlots.get(slotId);

            if (slot3 != null && slot3.getHasStack() && slot3.canTakeStack(player)) {
                ItemStack itemstack7 = slot3.decrStackSize(dragType == 0 ? 1 : slot3.getStack().getCount());
                slot3.onTake(player, itemstack7);
                player.dropItem(itemstack7, true);
            }
        } else if (clickType == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot2 = inventorySlots.get(slotId);
            ItemStack itemstack6 = inventory.getItemStack();

            if (!itemstack6.isEmpty() && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player))) {
                int i1 = dragType == 0 ? 0 : inventorySlots.size() - 1;
                int j1 = dragType == 0 ? 1 : -1;

                for (int i3 = 0; i3 < 2; ++i3) {
                    for (int j3 = i1; j3 >= 0 && j3 < inventorySlots.size() && itemstack6.getCount() < itemstack6.getMaxStackSize(); j3 += j1) {
                        Slot slot8 = inventorySlots.get(j3);

                        if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack6, true) && slot8.canTakeStack(player) && canMergeSlot(itemstack6, slot8)) {
                            ItemStack itemstack14 = slot8.getStack();

                            if (i3 != 0 || itemstack14.getCount() != itemstack14.getMaxStackSize()) {
                                int k3 = Math.min(itemstack6.getMaxStackSize() - itemstack6.getCount(), itemstack14.getCount());
                                ItemStack itemstack4 = slot8.decrStackSize(k3);
                                itemstack6.grow(k3);

                                if (itemstack4.isEmpty()) {
                                    slot8.putStack(ItemStack.EMPTY);
                                }

                                slot8.onTake(player, itemstack4);
                            }
                        }
                    }
                }
            }

            detectAndSendChanges();
        }

        return stack;
    }
}