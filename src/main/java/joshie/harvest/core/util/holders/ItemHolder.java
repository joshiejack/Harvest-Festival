package joshie.harvest.core.util.holders;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ItemHolder extends AbstractItemHolder {
    private final Item item;

    private ItemHolder(Item item) {
        this.item = item;
    }

    public static ItemHolder of(Item item) {
        return new ItemHolder(item);
    }

    @Override
    public boolean matches(@Nonnull ItemStack stack) {
        return stack.getItem() == item;
    }

    @Override
    public NonNullList<ItemStack> getMatchingStacks() {
        if (matchingStacks != null && matchingStacks.size() > 0) return matchingStacks;
        else {
            matchingStacks = NonNullList.create();
            matchingStacks.add(new ItemStack(item));
            return matchingStacks;
        }
    }

    public static ItemHolder readFromNBT(NBTTagCompound tag) {
        return new ItemHolder(Item.REGISTRY.getObject(new ResourceLocation(tag.getString("ItemName"))));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("ItemName", String.valueOf(Item.REGISTRY.getNameForObject(item)));
        return tag;
    }

    @Override
    public String toString() {
        return new ItemStack(item).getDisplayName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemHolder that = (ItemHolder) o;
        return item != null ? item.equals(that.item) : that.item == null;
    }

    @Override
    public int hashCode() {
        return item != null ? item.hashCode() : 0;
    }
}
