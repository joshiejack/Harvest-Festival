package joshie.harvest.core.helpers;

import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.items.HFItems;
import net.minecraft.item.ItemStack;

public class SizeableHelper {
    public static ItemStack getSizeable(int relationship, SizeableMeta meta, Size size) {
        return new ItemStack(HFItems.sized.get(meta), 1, size.ordinal());
    }

    public static Size getSize(int meta) {
        int size = Math.max(0, meta);
        return Size.values()[Math.min(2, meta)];
    }
}
