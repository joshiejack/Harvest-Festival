package joshie.harvest.core.util.holders;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ItemStackHolder extends AbstractItemHolder {
    private final Item item;
    private final int meta;

    private ItemStackHolder(Item item, int meta) {
        this.item = item;
        this.meta = meta;
    }

    public static ItemStackHolder of(ItemStack stack) {
        return new ItemStackHolder(stack.getItem(), stack.getItemDamage());
    }

    public static ItemStackHolder of(Item item, int meta) {
        return new ItemStackHolder(item, meta);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        matchingStacks = new ArrayList<>();
        matchingStacks.add(new ItemStack(item, 1, meta));
        return matchingStacks;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() == item && stack.getItemDamage() == meta;
    }

    public static ItemStackHolder readFromNBT(NBTTagCompound tag) {
        Item item = Item.REGISTRY.getObject(new ResourceLocation(tag.getString("ItemName")));
        int meta = tag.getInteger("ItemMeta");
        return new ItemStackHolder(item, meta);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("ItemName", String.valueOf(Item.REGISTRY.getNameForObject(item)));
        tag.setInteger("ItemMeta", meta);
        return tag;
    }

    @Override
    public String toString() {
        return new ItemStack(item, 1, meta).getDisplayName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemStackHolder that = (ItemStackHolder) o;
        return meta == that.meta && (item != null ? item.equals(that.item) : that.item == null);
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + meta;
        return result;
    }
}
