package joshie.harvest.core.util.holders;

import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.base.item.ItemHFSizeable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class SizeableHolder extends AbstractItemHolder {
    private final ItemStack stack;
    private final ISizedProvider item;
    private final Object object;

    private SizeableHolder(ItemStack stack) {
        this.stack = stack;
        this.item = (ItemHFSizeable) stack.getItem();
        this.object = this.item.getObject(stack);
    }

    public static SizeableHolder of(ItemStack stack) {
        return new SizeableHolder(stack);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ItemStack> getMatchingStacks() {
        if (matchingStacks != null && matchingStacks.size() > 0) return matchingStacks;
        else {
            matchingStacks = new ArrayList<>();
            matchingStacks.add(item.getStackOfSize(object, Size.SMALL, 1));
            matchingStacks.add(item.getStackOfSize(object, Size.MEDIUM, 1));
            matchingStacks.add(item.getStackOfSize(object, Size.LARGE, 1));
            return matchingStacks;
        }
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() == this.stack.getItem() && item.getObject(stack) == item.getObject(this.stack);
    }

    public static SizeableHolder readFromNBT(NBTTagCompound tag) {
        return new SizeableHolder(ItemStack.loadItemStackFromNBT(tag));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        return stack.writeToNBT(tag);
    }

    @Override
    public String toString() {
        return "SizeableHolder:" + (stack != null ? stack.getItem().getRegistryName() + " " + stack.getItemDamage() + " item: " + (item != null ? ((Item)item).getRegistryName() : "null") : "null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SizeableHolder that = (SizeableHolder) o;
        return item != null ? item.equals(that.item) : that.item == null && (object != null ? object.equals(that.object) : that.object == null);
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }
}
