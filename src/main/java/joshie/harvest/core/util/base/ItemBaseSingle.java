package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemBaseSingle<I extends ItemBaseSingle> extends ItemHFBase<I> {
    public ItemBaseSingle() {
        setCreativeTab(HFTab.FARMING);
    }

    public ItemBaseSingle(CreativeTabs tab) {
        super(tab);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.translate(super.getUnlocalizedName().replace("item.", ""));
    }
}