package joshie.harvest.animals.item;

import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.core.base.item.ItemHFSizeable;
import joshie.harvest.core.util.interfaces.ISizeable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemAnimalProduct extends ItemHFSizeable<ItemAnimalProduct, Sizeable> {
    public ItemAnimalProduct() {
        super(Sizeable.class);
    }

    @Override
    public int getHealAmount(@Nonnull ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case EGG:
            case MAYONNAISE:
                return 3 + getSize(stack).ordinal();
            case MILK:
                return 2 + getSize(stack).ordinal();
            default:
                return 0;
        }
    }

    @Override
    public float getSaturationModifier(@Nonnull ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case EGG:
                return 0.8F;
            case MAYONNAISE:
                return 1.0F;
            case MILK:
                return 0.6F;
            default:
                return 0;
        }
    }

    public enum Sizeable implements IStringSerializable, ISizeable {
        EGG(50, 60, 80), MILK(100, 150, 200), MAYONNAISE(70, 80, 100), WOOL(500, 750, 1000);

        private final long small;
        private final long medium;
        private final long large;

        Sizeable(long small, long medium, long large) {
            this.small = small;
            this.medium = medium;
            this.large = large;
        }

        @Override
        public long getSmall() {
            return small;
        }

        @Override
        public long getMedium() {
            return medium;
        }

        @Override
        public long getLarge() {
            return large;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

        @Override
        public int getMeta() {
            return ordinal();
        }
    }
}