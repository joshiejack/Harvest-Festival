package joshie.harvest.core.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.core.base.item.ItemHFSizeable;
import joshie.harvest.core.item.ItemSizeable.Sizeable;
import joshie.harvest.core.util.interfaces.ISizeable;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class ItemSizeable extends ItemHFSizeable<ItemSizeable, Sizeable> implements IShippable {
    public ItemSizeable() {
        super(Sizeable.class);
    }

    public enum Sizeable implements IStringSerializable, ISizeable {
        EGG(50, 60, 80), MILK(90, 120, 200), MAYONNAISE(70, 80, 100), WOOL(500, 750, 1000);

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