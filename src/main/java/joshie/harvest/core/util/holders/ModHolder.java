package joshie.harvest.core.util.holders;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ModHolder extends AbstractItemHolder {
    private final String mod;

    private ModHolder(String mod) {
        this.mod = mod;
    }

    public static ModHolder of(String mod) {
        return new ModHolder(mod);
    }

    @Override
    public NonNullList<ItemStack> getMatchingStacks() {
        return NonNullList.create();
    }

    @Override
    public boolean matches(@Nonnull ItemStack stack) {
        ResourceLocation resource = Item.REGISTRY.getNameForObject(stack.getItem());
        return resource != null && resource.getNamespace().equals(mod);
    }

    public static ModHolder readFromNBT(NBTTagCompound tag) {
        return new ModHolder(tag.getString("Mod"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("Mod", mod);
        return tag;
    }

    @Override
    public String toString() {
        return mod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModHolder modHolder = (ModHolder) o;
        return mod != null ? mod.equals(modHolder.mod) : modHolder.mod == null;
    }

    @Override
    public int hashCode() {
        return mod != null ? mod.hashCode() : 0;
    }
}
