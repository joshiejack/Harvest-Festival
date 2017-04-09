package joshie.harvest.core.util.holders;

import joshie.harvest.api.core.MatchType;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class OreHolder extends AbstractItemHolder {
    private final String ore;
    private final MatchType type;

    private OreHolder(String mod, MatchType type) {
        this.ore = mod;
        this.type = type;
    }

    public static OreHolder of(String mod, MatchType type) {
        return new OreHolder(mod, type);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        switch (type) {
            case FULL:
                return OreDictionary.getOres(ore);
            case PREFIX:
                return InventoryHelper.getStarts(ore);
            case SUFFIX:
                return InventoryHelper.getEnds(ore);
            case CONTAINS:
                return InventoryHelper.getContains(ore);
            default:
                return new ArrayList<>();
        }
    }

    @Override
    public boolean matches(ItemStack stack) {
        switch (type) {
            case FULL:
                return InventoryHelper.ORE_DICTIONARY.matches(stack, ore);
            case PREFIX:
                return InventoryHelper.startsWith(stack, ore);
            case SUFFIX:
                return InventoryHelper.endsWith(stack, ore);
            case CONTAINS:
                return InventoryHelper.contains(stack, ore);
            default:
                return false;
        }
    }

    public static OreHolder readFromNBT(NBTTagCompound tag) {
        return new OreHolder(tag.getString("Ore"), MatchType.valueOf(tag.getString("Type")));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("Ore", ore);
        tag.setString("Type", type.name());
        return tag;
    }

    @Override
    public String toString() {
        return ore;
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
