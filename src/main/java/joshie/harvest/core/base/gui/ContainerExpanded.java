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
import java.util.Iterator;

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
    public ItemStack specialClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack itemstack = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;
        ItemStack itemstack8;
        int k1;
        int k3;
        ItemStack itemstack11;
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
                Slot slot7 = (Slot)this.inventorySlots.get(slotId);
                itemstack11 = inventoryplayer.getItemStack();
                if (slot7 != null && canAddItemToSlot(slot7, itemstack11, true) && slot7.isItemValid(itemstack11) && (this.dragMode == 2 || itemstack11.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7)) {
                    this.dragSlots.add(slot7);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    itemstack8 = inventoryplayer.getItemStack().copy();
                    k1 = inventoryplayer.getItemStack().getCount();
                    Iterator var23 = this.dragSlots.iterator();

                    label359:
                    while(true) {
                        Slot slot8;
                        ItemStack itemstack13;
                        do {
                            do {
                                do {
                                    do {
                                        if (!var23.hasNext()) {
                                            itemstack8.setCount(k1);
                                            inventoryplayer.setItemStack(itemstack8);
                                            break label359;
                                        }

                                        slot8 = (Slot)var23.next();
                                        itemstack13 = inventoryplayer.getItemStack();
                                    } while(slot8 == null);
                                } while(!canAddItemToSlot(slot8, itemstack13, true));
                            } while(!slot8.isItemValid(itemstack13));
                        } while(this.dragMode != 2 && itemstack13.getCount() < this.dragSlots.size());

                        if (this.canDragIntoSlot(slot8)) {
                            ItemStack itemstack14 = itemstack8.copy();
                            int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
                            k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getItemStackLimit(itemstack14));
                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            slot8.putStack(itemstack14);
                        }
                    }
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else {
            Slot slot6;
            int k2;
            if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
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

                    slot6 = (Slot)this.inventorySlots.get(slotId);
                    if (slot6 == null || !slot6.canTakeStack(player)) {
                        return ItemStack.EMPTY;
                    }

                    for(itemstack8 = this.transferStackInSlot(player, slotId); !itemstack8.isEmpty() && ItemStack.areItemsEqual(slot6.getStack(), itemstack8); itemstack8 = this.transferStackInSlot(player, slotId)) {
                        itemstack = itemstack8.copy();
                    }
                } else {
                    if (slotId < 0) {
                        return ItemStack.EMPTY;
                    }

                    slot6 = (Slot)this.inventorySlots.get(slotId);
                    if (slot6 != null) {
                        itemstack8 = slot6.getStack();
                        itemstack11 = inventoryplayer.getItemStack();
                        if (!itemstack8.isEmpty()) {
                            itemstack = itemstack8.copy();
                        }

                        if (itemstack8.isEmpty()) {
                            if (!itemstack11.isEmpty() && slot6.isItemValid(itemstack11)) {
                                k2 = dragType == 0 ? itemstack11.getCount() : 1;
                                if (k2 > slot6.getItemStackLimit(itemstack11)) {
                                    k2 = slot6.getItemStackLimit(itemstack11);
                                }

                                slot6.putStack(itemstack11.splitStack(k2));
                            }
                        } else if (slot6.canTakeStack(player)) {
                            if (itemstack11.isEmpty()) {
                                if (itemstack8.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                    inventoryplayer.setItemStack(ItemStack.EMPTY);
                                } else {
                                    k2 = dragType == 0 ? itemstack8.getCount() : (itemstack8.getCount() + 1) / 2;
                                    inventoryplayer.setItemStack(slot6.decrStackSize(k2));
                                    if (itemstack8.isEmpty()) {
                                        slot6.putStack(ItemStack.EMPTY);
                                    }

                                    slot6.onTake(player, inventoryplayer.getItemStack());
                                }
                            } else if (slot6.isItemValid(itemstack11)) {
                                if (itemstack8.getItem() == itemstack11.getItem() && itemstack8.getMetadata() == itemstack11.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack8, itemstack11)) {
                                    k2 = dragType == 0 ? itemstack11.getCount() : 1;
                                    if (k2 > slot6.getItemStackLimit(itemstack11) - itemstack8.getCount()) {
                                        k2 = slot6.getItemStackLimit(itemstack11) - itemstack8.getCount();
                                    }

                                    if (k2 > itemstack11.getMaxStackSize() - itemstack8.getCount()) {
                                        k2 = itemstack11.getMaxStackSize() - itemstack8.getCount();
                                    }

                                    itemstack11.shrink(k2);
                                    itemstack8.grow(k2);
                                } else if (itemstack11.getCount() <= slot6.getItemStackLimit(itemstack11)) {
                                    slot6.putStack(itemstack11);
                                    inventoryplayer.setItemStack(itemstack8);
                                }
                            } else if (itemstack8.getItem() == itemstack11.getItem() && itemstack11.getMaxStackSize() > 1 && (!itemstack8.getHasSubtypes() || itemstack8.getMetadata() == itemstack11.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack8, itemstack11) && !itemstack8.isEmpty()) {
                                k2 = itemstack8.getCount();
                                if (k2 + itemstack11.getCount() <= itemstack11.getMaxStackSize()) {
                                    itemstack11.grow(k2);
                                    itemstack8 = slot6.decrStackSize(k2);
                                    if (itemstack8.isEmpty()) {
                                        slot6.putStack(ItemStack.EMPTY);
                                    }

                                    slot6.onTake(player, inventoryplayer.getItemStack());
                                }
                            }
                        }

                        slot6.onSlotChanged();
                    }
                }
            } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
                slot6 = (Slot)this.inventorySlots.get(slotId);
                itemstack8 = inventoryplayer.getStackInSlot(dragType);
                itemstack11 = slot6.getStack();
                if (!itemstack8.isEmpty() || !itemstack11.isEmpty()) {
                    if (itemstack8.isEmpty()) {
                        if (slot6.canTakeStack(player)) {
                            inventoryplayer.setInventorySlotContents(dragType, itemstack11);
                            // WARNING: slot6.onSwapCraft(itemstack11.getCount());
                            slot6.putStack(ItemStack.EMPTY);
                            slot6.onTake(player, itemstack11);
                        }
                    } else if (itemstack11.isEmpty()) {
                        if (slot6.isItemValid(itemstack8)) {
                            k2 = slot6.getItemStackLimit(itemstack8);
                            if (itemstack8.getCount() > k2) {
                                slot6.putStack(itemstack8.splitStack(k2));
                            } else {
                                slot6.putStack(itemstack8);
                                inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
                            }
                        }
                    } else if (slot6.canTakeStack(player) && slot6.isItemValid(itemstack8)) {
                        k2 = slot6.getItemStackLimit(itemstack8);
                        if (itemstack8.getCount() > k2) {
                            slot6.putStack(itemstack8.splitStack(k2));
                            slot6.onTake(player, itemstack11);
                            if (!inventoryplayer.addItemStackToInventory(itemstack11)) {
                                player.dropItem(itemstack11, true);
                            }
                        } else {
                            slot6.putStack(itemstack8);
                            inventoryplayer.setInventorySlotContents(dragType, itemstack11);
                            slot6.onTake(player, itemstack11);
                        }
                    }
                }
            } else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
                slot6 = (Slot)this.inventorySlots.get(slotId);
                if (slot6 != null && slot6.getHasStack()) {
                    itemstack8 = slot6.getStack().copy();
                    itemstack8.setCount(itemstack8.getMaxStackSize());
                    inventoryplayer.setItemStack(itemstack8);
                }
            } else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
                slot6 = (Slot)this.inventorySlots.get(slotId);
                if (slot6 != null && slot6.getHasStack() && slot6.canTakeStack(player)) {
                    itemstack8 = slot6.decrStackSize(dragType == 0 ? 1 : slot6.getStack().getCount());
                    slot6.onTake(player, itemstack8);
                    player.dropItem(itemstack8, true);
                }
            } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
                slot6 = (Slot)this.inventorySlots.get(slotId);
                itemstack8 = inventoryplayer.getItemStack();
                if (!itemstack8.isEmpty() && (slot6 == null || !slot6.getHasStack() || !slot6.canTakeStack(player))) {
                    k1 = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                    k2 = dragType == 0 ? 1 : -1;

                    for(int k = 0; k < 2; ++k) {
                        for(int l = k1; l >= 0 && l < this.inventorySlots.size() && itemstack8.getCount() < itemstack8.getMaxStackSize(); l += k2) {
                            Slot slot1 = (Slot)this.inventorySlots.get(l);
                            if (slot1.getHasStack() && canAddItemToSlot(slot1, itemstack8, true) && slot1.canTakeStack(player) && this.canMergeSlot(itemstack8, slot1)) {
                                ItemStack itemstack2 = slot1.getStack();
                                if (k != 0 || itemstack2.getCount() != itemstack2.getMaxStackSize()) {
                                    k3 = Math.min(itemstack8.getMaxStackSize() - itemstack8.getCount(), itemstack2.getCount());
                                    ItemStack itemstack3 = slot1.decrStackSize(k3);
                                    itemstack8.grow(k3);
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
        }

        return itemstack;
    }
}