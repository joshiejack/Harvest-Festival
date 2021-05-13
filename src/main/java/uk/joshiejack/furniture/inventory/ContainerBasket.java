package uk.joshiejack.furniture.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import uk.joshiejack.economy.shipping.ShippingRegistry;
import uk.joshiejack.furniture.FurnitureItems;
import uk.joshiejack.penguinlib.inventory.ContainerPenguinInventory;
import uk.joshiejack.penguinlib.inventory.slot.SlotPenguin;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;

import javax.annotation.Nonnull;

public class ContainerBasket extends ContainerPenguinInventory {
    private final ItemStackHandler handler;
    private final ItemStack basket;
    private int lastSlot;

    public ContainerBasket(InventoryPlayer inventory, ItemStack basket) {
        super(64);
        this.handler = new ItemStackHandler(64);
        this.basket = basket;
        NBTTagCompound tag = NBTHelper.getItemNBT(basket);
        this.handler.deserializeNBT(tag.getCompoundTag("Inventory"));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new BasketSlotItemHandler(handler, j + i * 9, 8 + j * 18, (i * 18) + 18));
            }
        }

        bindPlayerInventory(inventory, 1);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        NBTTagCompound tag = NBTHelper.getItemNBT(basket);
        tag.setInteger("AppearanceSlot", lastSlot);
        tag.setTag("Inventory", handler.serializeNBT());
    }

    @Override
    protected Slot createSlot(InventoryPlayer inventory, int id, int x, int y) {
        return new Slot(inventory, id, x, y) {
            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return getStack().getItem() != FurnitureItems.BASKET;
            }
        };
    }

    private class BasketSlotItemHandler extends SlotPenguin {
        BasketSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();
            lastSlot = slotNumber;
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return ShippingRegistry.INSTANCE.getValue(stack) > 0;
        }
    }
}
