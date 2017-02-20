package joshie.harvest.core.util.holders;

import joshie.harvest.core.util.interfaces.IFMLItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class FMLHolder extends AbstractItemHolder {
    private final IFMLItem item;
    private final ResourceLocation resource;

    private FMLHolder(ItemStack stack) {
        this.item = (IFMLItem) stack.getItem();
        this.resource = item.getObjectFromStack(stack).getRegistryName();
    }

    private FMLHolder(IFMLItem item, ResourceLocation resource) {
        this.item = item;
        this.resource = resource;
    }

    public static FMLHolder of(ItemStack stack) {
        return new FMLHolder(stack);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        matchingStacks = new ArrayList<>();
        matchingStacks.add(item.getStackFromResource(resource));
        return matchingStacks;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() == item && item.getObjectFromStack(stack).getRegistryName().equals(resource);
    }

    public static FMLHolder readFromNBT(NBTTagCompound tag) {
        IFMLItem item = (IFMLItem) Item.REGISTRY.getObject(new ResourceLocation(tag.getString("ItemName")));
        ResourceLocation resource = new ResourceLocation(tag.getString("Resource"));
        return new FMLHolder(item, resource);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("ItemName", Item.REGISTRY.getNameForObject((Item)item).toString());
        tag.setString("Resource", resource.toString());
        return tag;
    }

    @Override
    public String toString() {
        return "FMLHolder:"  + (resource != null ? resource.toString() + (item != null ? ((Item)item).getRegistryName().toString() : "")  : "null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FMLHolder fmlHolder = (FMLHolder) o;
        if (item != null ? !item.equals(fmlHolder.item) : fmlHolder.item != null) return false;
        return resource != null ? resource.equals(fmlHolder.resource) : fmlHolder.resource == null;

    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        return result;
    }
}
