package uk.joshiejack.penguinlib.util.handlers;

import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetInventorySlot;
import uk.joshiejack.penguinlib.tile.inventory.TileInventory;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ValidatedStackHandler extends ItemStackHandler {
    public static final int NO_SLOT_FOUND = -1;
    private final TileInventory tile;
    private int players;

    public ValidatedStackHandler(TileInventory tile, int size) {
        super(size);
        this.tile = tile;
    }

    public int getCount(Holder search) {
        int i = 0;
        for (ItemStack stack: stacks) {
            if (!stack.isEmpty() && search.matches(stack)) i += stack.getCount();
        }

        return i;
    }

    public int remove(ItemStack target) {
        int count = target.getCount();
        for (ItemStack stack: stacks) {
            if (!stack.isEmpty() && stack.isItemEqual(target)) {
                int shrink = MathsHelper.constrainToRangeInt(target.getCount(), 0, stack.getCount());
                if (shrink > 0) {
                    stack.shrink(1);
                    count -= shrink;
                }
            }

            if (count <= 0) return 0;
        }

        tile.markDirty();
        return count;
    }

    public boolean hasAny(NonNullList<ItemStack> list) {
       return stacks.stream().anyMatch((internal) -> list.stream().anyMatch(internal::isItemEqual));
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 512;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize() * 8);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!tile.isStackValidForInsertion(slot, stack)) return stack;
        else return super.insertItem(slot, stack, simulate);
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!tile.isSlotValidForExtraction(slot, amount)) return ItemStack.EMPTY;
        else return super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (!tile.getWorld().isRemote) {
            tile.onContentsChanged(slot);
            tile.markDirty();
            PenguinNetwork.sendToNearby(tile, new PacketSetInventorySlot(tile.getPos(), slot, getStackInSlot(slot)));
        }
    }

    public int findEmptySlot(int finishSlot) {
        for (int i = 0; i < finishSlot; i++) {
            if (getStackInSlot(i).isEmpty()) return i;
        }

        return NO_SLOT_FOUND;
    }

    public int getPlayersWatching() {
        return players;
    }

    public void addPlayer() {
        this.players = MathsHelper.constrainToRangeInt(players + 1, 0, 10);
    }

    public void removePlayer() {
        this.players = MathsHelper.constrainToRangeInt(players - 1, 0, 10);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isEmpty()) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                if (getSlotLimit(i) > 64) {
                    StackHelper.writeToNBT(stacks.get(i), itemTag);
                } else stacks.get(i).writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)  {
        setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.size());
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)  {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < stacks.size())  {
                if (getSlotLimit(slot) > 64) {
                    stacks.set(slot, StackHelper.readFromNBT(itemTags));
                } else stacks.set(slot, new ItemStack(itemTags));
            }
        }

        onLoad();
    }
}
