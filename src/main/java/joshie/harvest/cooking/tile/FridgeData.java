package joshie.harvest.cooking.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class FridgeData implements IInventory {
    protected final NonNullList<ItemStack> inventory = NonNullList.withSize(54, ItemStack.EMPTY);
    private final TileFridge tile;
    private int players;

    public FridgeData(TileFridge tile) {
        this.tile = tile;
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(this.inventory, index, count);

        if (!stack.isEmpty()) {
            this.markDirty();
        }

        return stack;
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        if (!inventory.get(index).isEmpty()) {
            ItemStack stack = inventory.get(index);
            inventory.get(index).isEmpty();
            return stack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
        inventory.set(slot, stack);

        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }

        markDirty();
    }

    @Override
    @Nonnull
    public String getName() {
        return TextHelper.translate("cookware.fridge");
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TextComponentString("hi");
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
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
        players++;
        if (players < 0) {
            players = 0;
        }

        player.world.addBlockEvent(tile.getPos(), tile.getBlockType(), 1, players);
        tile.getWorld().playSound(null, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), HFSounds.FRIDGE, SoundCategory.BLOCKS, 2F, tile.getWorld().rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        players--;
        if (players < 0) {
            players = 0;
        }

        player.world.addBlockEvent(tile.getPos(), tile.getBlockType(), 1, players);
        tile.getWorld().playSound(null, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), HFSounds.FRIDGE, SoundCategory.BLOCKS, 2F, tile.getWorld().rand.nextFloat() * 0.1F + 0.9F);
        tile.saveAndRefresh(); //Update the client about this
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
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
    public void clear() {
        inventory.clear();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("FridgeContents", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, NBTHelper.readItemStack(tag));
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
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