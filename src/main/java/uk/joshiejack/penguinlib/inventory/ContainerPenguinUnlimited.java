package uk.joshiejack.penguinlib.inventory;

import uk.joshiejack.penguinlib.inventory.slot.SlotUnlimited;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class ContainerPenguinUnlimited extends ContainerPenguinInventory {
    public ContainerPenguinUnlimited(int inventorySize) {
        super(inventorySize);
    }

    protected boolean hasUnlimitedInventory(Slot slot) {
        return slot instanceof SlotUnlimited;
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotID, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        Slot slot = slotID < 0 || slotID > inventorySlots.size() ? null : inventorySlots.get(slotID);
        return hasUnlimitedInventory(slot) ? specialClick(slotID, dragType, clickTypeIn, player) : super.slotClick(slotID, dragType, clickTypeIn, player);
    }

    private ItemStack specialClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack itemstack = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int j1 = this.dragEvent;
            this.dragEvent = getDragEvent(dragType);

            if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
                this.resetDrag();
            } else if (inventoryplayer.getItemStack().isEmpty()) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(dragType);

                if (isValidDragMode(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot7 = this.inventorySlots.get(slotId);
                ItemStack itemstack12 = inventoryplayer.getItemStack();

                if (slot7 != null && canAddItemToSlot(slot7, itemstack12, true) && slot7.isItemValid(itemstack12) && (this.dragMode == 2 || itemstack12.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7)) {
                    this.dragSlots.add(slot7);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack itemstack9 = inventoryplayer.getItemStack().copy();
                    int k1 = inventoryplayer.getItemStack().getCount();

                    for (Slot slot8 : this.dragSlots) {
                        ItemStack itemstack13 = inventoryplayer.getItemStack();

                        if (slot8 != null && canAddItemToSlot(slot8, itemstack13, true) && slot8.isItemValid(itemstack13) && (this.dragMode == 2 || itemstack13.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(slot8)) {
                            ItemStack itemstack14 = itemstack9.copy();
                            int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
                            int k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getItemStackLimit(itemstack14));

                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            slot8.putStack(itemstack14);
                        }
                    }

                    itemstack9.setCount(k1);
                    inventoryplayer.setItemStack(itemstack9);
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!inventoryplayer.getItemStack().isEmpty()) {
                    if (dragType == 0) {
                        player.dropItem(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack(ItemStack.EMPTY);
                    }

                    if (dragType == 1) {
                        player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot5 = this.inventorySlots.get(slotId);

                if (slot5 == null || !slot5.canTakeStack(player)) {
                    return ItemStack.EMPTY;
                }

                for (ItemStack itemstack7 = this.transferStackInSlot(player, slotId); !itemstack7.isEmpty() && ItemStack.areItemsEqual(slot5.getStack(), itemstack7); itemstack7 = this.transferStackInSlot(player, slotId)) {
                    itemstack = itemstack7.copy();
                }
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot = this.inventorySlots.get(slotId);

                if (slot != null) {
                    ItemStack inSlot = slot.getStack();
                    ItemStack held = inventoryplayer.getItemStack();

                    if (!inSlot.isEmpty()) {
                        itemstack = inSlot.copy();
                    }

                    if (inSlot.isEmpty()) {
                        if (!held.isEmpty() && slot.isItemValid(held)) {
                            int i3 = dragType == 0 ? held.getCount() : 1;

                            if (i3 > slot.getItemStackLimit(held)) {
                                //i3 = slot.getItemStackLimit(held);
                            }

                            slot.putStack(held.splitStack(i3));
                        }
                    } else if (slot.canTakeStack(player)) {
                        if (held.isEmpty()) {
                            if (inSlot.isEmpty()) {
                                slot.putStack(ItemStack.EMPTY);
                                inventoryplayer.setItemStack(ItemStack.EMPTY);
                            } else {
                                int l2 = dragType == 0 ? inSlot.getCount() : (inSlot.getCount() + 1) / 2;
                                if (dragType == 0 && l2 > inSlot.getMaxStackSize()) {
                                    l2 = inSlot.getMaxStackSize();
                                } else if (dragType == 1 && l2 > inSlot.getMaxStackSize() / 2) {
                                    l2 = inSlot.getMaxStackSize() / 2;
                                }

                                inventoryplayer.setItemStack(inSlot.splitStack(l2));

                                if (inSlot.isEmpty()) {
                                    slot.putStack(ItemStack.EMPTY);
                                }

                                slot.onTake(player, inventoryplayer.getItemStack());
                            }
                        } else if (slot.isItemValid(held)) {
                            if (inSlot.getItem() == held.getItem() && inSlot.getMetadata() == held.getMetadata() && ItemStack.areItemStackTagsEqual(inSlot, held)) {
                                int k2 = dragType == 0 ? held.getCount() : 1;

                                if (k2 > slot.getItemStackLimit(held) - inSlot.getCount()) {
                                    k2 = slot.getItemStackLimit(held) - inSlot.getCount();
                                }

                                held.shrink(k2);
                                inSlot.grow(k2);
                            } else if (inSlot.getCount() <= inSlot.getMaxStackSize()) {
                                slot.putStack(held);
                                inventoryplayer.setItemStack(inSlot);
                            }
                        } else if (inSlot.getItem() == held.getItem() && held.getMaxStackSize() > 1 && (!inSlot.getHasSubtypes() || inSlot.getMetadata() == held.getMetadata()) && ItemStack.areItemStackTagsEqual(inSlot, held) && !inSlot.isEmpty()) {
                            int j2 = inSlot.getCount();

                            if (j2 + held.getCount() <= held.getMaxStackSize()) {
                                held.grow(j2);
                                inSlot = slot.decrStackSize(j2);

                                if (inSlot.isEmpty()) {
                                    slot.putStack(ItemStack.EMPTY);
                                }

                                slot.onTake(player, inventoryplayer.getItemStack());
                            }
                        }
                    }

                    slot.onSlotChanged();
                }
            }
        } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            Slot slot4 = this.inventorySlots.get(slotId);
            ItemStack held = inventoryplayer.getStackInSlot(dragType);
            ItemStack inSlot = slot4.getStack();

            if (!held.isEmpty() || !inSlot.isEmpty()) {
                if (held.isEmpty()) {
                    if (slot4.canTakeStack(player)) {
                        inventoryplayer.setInventorySlotContents(dragType, inSlot);
                        slot4.putStack(ItemStack.EMPTY);
                        slot4.onTake(player, inSlot);
                    }
                } else if (inSlot.isEmpty()) {
                    if (slot4.isItemValid(held)) {
                        int l1 = slot4.getItemStackLimit(held);

                        if (held.getCount() > l1) {
                            slot4.putStack(held.splitStack(l1));
                        } else {
                            slot4.putStack(held);
                            inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
                        }
                    }
                } else if (slot4.canTakeStack(player) && slot4.isItemValid(held) && inSlot.getCount() <= inSlot.getMaxStackSize()) {
                    int i2 = slot4.getItemStackLimit(held);

                    if (held.getCount() > i2) {
                        slot4.putStack(held.splitStack(i2));
                        slot4.onTake(player, inSlot);

                        if (!inventoryplayer.addItemStackToInventory(inSlot)) {
                            player.dropItem(inSlot, true);
                        }
                    } else {
                        slot4.putStack(held);
                        inventoryplayer.setInventorySlotContents(dragType, inSlot);
                        slot4.onTake(player, inSlot);
                    }
                }
            }
        } else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot3 = this.inventorySlots.get(slotId);

            if (slot3 != null && slot3.getHasStack()) {
                ItemStack itemstack5 = slot3.getStack().copy();
                itemstack5.setCount(itemstack5.getMaxStackSize());
                inventoryplayer.setItemStack(itemstack5);
            }
        } else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot2 = this.inventorySlots.get(slotId);

            if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player)) {
                ItemStack itemstack4 = slot2.decrStackSize(dragType == 0 ? 1 : slot2.getStack().getCount());
                slot2.onTake(player, itemstack4);
                player.dropItem(itemstack4, true);
            }
        } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack itemstack1 = inventoryplayer.getItemStack();

            if (!itemstack1.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player))) {
                int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                int j = dragType == 0 ? 1 : -1;

                for (int k = 0; k < 2; ++k) {
                    for (int l = i; l >= 0 && l < this.inventorySlots.size() && itemstack1.getCount() < itemstack1.getMaxStackSize(); l += j) {
                        Slot slot1 = this.inventorySlots.get(l);

                        if (slot1.getHasStack() && canAddItemToSlot(slot1, itemstack1, true) && slot1.canTakeStack(player) && this.canMergeSlot(itemstack1, slot1)) {
                            ItemStack itemstack2 = slot1.getStack();

                            if (k != 0 || itemstack2.getCount() != itemstack2.getMaxStackSize()) {
                                int i1 = Math.min(itemstack1.getMaxStackSize() - itemstack1.getCount(), itemstack2.getCount());
                                ItemStack itemstack3 = slot1.decrStackSize(i1);
                                itemstack1.grow(i1);

                                if (itemstack3.isEmpty()) {
                                    slot1.putStack(ItemStack.EMPTY);
                                }

                                slot1.onTake(player, itemstack3);
                            }
                        }
                    }
                }
            }

            this.detectAndSendChanges();
        }

        return itemstack;
    }

    @Override
    protected boolean mergeItemStack(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
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

                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();

                if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = slot.getSlotStackLimit();

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

                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() > slot1.getSlotStackLimit()) {
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
}
