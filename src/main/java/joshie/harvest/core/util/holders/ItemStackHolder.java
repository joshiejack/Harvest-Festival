package joshie.harvest.core.util.holders;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

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
        tag.setString("ItemName", Item.REGISTRY.getNameForObject(item).toString());
        tag.setInteger("ItemMeta", meta);
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemStackHolder that = (ItemStackHolder) o;
        if (meta != that.meta) return false;
        return item != null ? item.equals(that.item) : that.item == null;

    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + meta;
        return result;
    }
}
