package joshie.harvest.core.util.holder;

import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreHolder extends AbstractItemHolder {
    private final String ore;

    private OreHolder(String mod) {
        this.ore = mod;
    }

    public static OreHolder of(String mod) {
        return new OreHolder(mod);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        return OreDictionary.getOres(ore);
    }

    @Override
    public boolean matches(ItemStack stack) {
        return InventoryHelper.ORE_DICTIONARY.matches(stack, ore);
    }

    public static OreHolder readFromNBT(NBTTagCompound tag) {
        return new OreHolder(tag.getString("Ore"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("Ore", ore);
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OreHolder modHolder = (OreHolder) o;
        return ore != null ? ore.equals(modHolder.ore) : modHolder.ore == null;
    }

    @Override
    public int hashCode() {
        return ore != null ? ore.hashCode() : 0;
    }
}
