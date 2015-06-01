package joshie.harvest.player;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.core.helpers.ServerHelper;
import joshie.harvest.core.util.IData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class FridgeContents implements IInventory, IData {
    private ItemStack[] inventory;
    private World world;

    public FridgeContents(World world) {
        inventory = new ItemStack[64];
        this.world = world;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (inventory[slot] != null) {
            ItemStack stack;

            if (inventory[slot].stackSize <= amount) {
                stack = inventory[slot];
                inventory[slot] = null;

                markDirty();
                return stack;
            } else {
                stack = inventory[slot].splitStack(amount);

                if (inventory[slot].stackSize == 0) {
                    inventory[slot] = null;
                }

                markDirty();
                return stack;
            }
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (inventory[slot] != null) {
            ItemStack stack = inventory[slot];
            inventory[slot] = null;
            return stack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (!world.isRemote) {
            ServerHelper.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return HFApi.COOKING.getCookingComponents(stack).size() > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("FridgeContents", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        nbt.setTag("FridgeContents", itemList);
    }
}
