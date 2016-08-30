package joshie.harvest.core.lib;

import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.handlers.SizeableRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class Sizeable extends IForgeRegistryEntry.Impl<Sizeable>  {
    private final String unlocalized;
    private long small, medium, large;

    public Sizeable(String unlocalized, long small, long medium, long large) {
        this.unlocalized = unlocalized;
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public boolean matches(ItemStack stack) {
        return HFCore.SIZEABLE.getObjectFromStack(stack) == this;
    }

    public ItemStack getStack(Size size) {
        return getStackOfSize(size, 1);
    }

    public ItemStack getStackOfSize(Size size, int stackSize) {
        int sizeableID = SizeableRegistry.REGISTRY.getValues().indexOf(this);
        int sizeID = size.ordinal();
        return new ItemStack(HFCore.SIZEABLE, stackSize, (sizeableID * 3) + sizeID);
    }

    public long getValue(Size size) {
        if (size == Size.LARGE) return large;
        else if (size == Size.MEDIUM) return medium;
        else return small;
    }

    public String getUnlocalizedName() {
        return unlocalized;
    }
}