package joshie.harvest.mining.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ISellable;
import joshie.harvest.mining.item.ItemDarkDrop.DarkDrop;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemDarkDrop extends ItemHFEnum<ItemDarkDrop, DarkDrop> {
    public enum DarkDrop implements IStringSerializable, ISellable {
        FEATHER(50L), LEATHER(150L);

        private final long sell;

        DarkDrop(long sell) {
            this.sell = sell;
        }

        @Override
        public long getSellValue() {
            return sell;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemDarkDrop() {
        super(HFTab.MINING, DarkDrop.class);
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return CreativeSort.NONE;
    }
}