package joshie.harvest.animals.item;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.core.base.item.ItemHFSizeable;
import joshie.harvest.core.util.interfaces.ISizeable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class ItemAnimalProduct extends ItemHFSizeable<ItemAnimalProduct, Sizeable> {
    public ItemAnimalProduct() {
        super(Sizeable.class);
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case EGG:
            case MAYONNAISE:
                return 3;
            case MILK:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
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
        EGG(50, 60, 80, 120), MILK(90, 120, 200, 300), MAYONNAISE(70, 80, 100, 150), WOOL(500, 750, 1000, 1500);

        private final long small;
        private final long medium;
        private final long large;
        private final long golden;

        Sizeable(long small, long medium, long large, long golden) {
            this.small = small;
            this.medium = medium;
            this.large = large;
            this.golden = golden;
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
        public long getGolden() {
            return golden;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

        @Override
        public int getMeta() {
            return ordinal();
        }

        @Override
        public ItemStack getGoldenProduct() {
            return HFAnimals.GOLDEN_PRODUCT.getStackFromEnum(this);
        }
    }
}