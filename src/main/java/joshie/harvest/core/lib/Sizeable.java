package joshie.harvest.core.lib;

import joshie.harvest.api.core.ISizeable;
import net.minecraft.item.Item;

public class Sizeable extends net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl<Sizeable> implements ISizeable {
    private long small, medium, large;
    private Item item;

    public Sizeable(long small, long medium, long large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public long getValue(Size size) {
        if (size == Size.LARGE) return large;
        else if (size == Size.MEDIUM) return medium;
        else return small;
    }
}