package joshie.harvest.cooking.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.tile.TileFridge;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;

public class FridgeData implements IInventory {
    protected ItemStack[] inventory;
    private TileFridge tile;
    private int players;

    public FridgeData(TileFridge tile) {
        inventory = new ItemStack[54];
        this.tile = tile;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, index, count);

        if (itemstack != null) {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (inventory[index] != null) {
            ItemStack stack = inventory[index];
            inventory[index] = null;
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
    public String getName() {
        return "";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 512;
    }

    @Override
    public void markDirty() {
        this.tile.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        players++;
        if (players < 0) {
            players = 0;
        }

        player.worldObj.addBlockEvent(tile.getPos(), tile.getBlockType(), 1, players);
        tile.getWorld().playSound(null, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), HFSounds.FRIDGE, SoundCategory.BLOCKS, 2F, tile.getWorld().rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        players--;
        if (players < 0) {
            players = 0;
        }

        player.worldObj.addBlockEvent(tile.getPos(), tile.getBlockType(), 1, players);
        tile.getWorld().playSound(null, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), HFSounds.FRIDGE, SoundCategory.BLOCKS, 2F, tile.getWorld().rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return HFApi.cooking.isIngredient(stack);
    }


    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        if (id == 1) players = value;

    }

    @Override
    public int getFieldCount() {
        return players;
    }

    @Override
    public void clear() {}

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("FridgeContents", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = NBTHelper.readItemStack(tag);
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                NBTHelper.writeItemStack(stack, tag);
                itemList.appendTag(tag);
            }
        }

        nbt.setTag("FridgeContents", itemList);
        return nbt;
    }
}