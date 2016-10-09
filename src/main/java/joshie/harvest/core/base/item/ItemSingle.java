package joshie.harvest.core.base.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemSingle<I extends ItemSingle> extends ItemHFBase<I> {
    public ItemSingle() {
        setCreativeTab(HFTab.FARMING);
    }

    public ItemSingle(CreativeTabs tab) {
        super(tab);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TextHelper.translate(super.getUnlocalizedName().replace("item.", ""));
    }
}