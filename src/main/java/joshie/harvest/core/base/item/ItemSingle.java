package joshie.harvest.core.base.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemSingle<I extends ItemSingle> extends ItemHFBase<I> {
    public ItemSingle() {
        setCreativeTab(HFTab.FARMING);
    }

    public ItemSingle(CreativeTabs tab) {
        super(tab);
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return TextHelper.translate(super.getTranslationKey().replace("item.", ""));
    }
}