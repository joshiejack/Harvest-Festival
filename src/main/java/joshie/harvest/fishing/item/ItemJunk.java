package joshie.harvest.fishing.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class ItemJunk extends ItemHFEnum<ItemJunk, Junk> implements IShippable {
    public ItemJunk() {
        super(HFTab.FISHING, Junk.class);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return getEnumFromStack(stack).getSellValue();
    }

    public enum Junk implements IStringSerializable {
        CAN(1L), BOOT(1L), TREASURE(10000L), BONES(1L), FOSSIL(5000L), BAIT(10L, 1L);

        private final long cost;
        private final long sell;

        Junk(long sell) {
            this.cost = 0L;
            this.sell = sell;
        }

        Junk(long cost, long sell) {
            this.cost = cost;
            this.sell = sell;
        }

        public long getCost() {
            return cost;
        }

        public long getSellValue() {
            return sell;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
