package joshie.harvest.cooking.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.ItemHFBase;
import net.minecraft.item.ItemStack;

public class ItemCookbook extends ItemHFBase<ItemCookbook> implements ICreativeSorted {
    public ItemCookbook() {
        super(HFTab.COOKING);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 1000;
    }
}
