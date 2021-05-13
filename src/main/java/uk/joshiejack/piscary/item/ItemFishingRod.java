package uk.joshiejack.piscary.item;

import uk.joshiejack.penguinlib.item.base.ItemBaseFishingRod;
import uk.joshiejack.piscary.Piscary;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static uk.joshiejack.piscary.Piscary.MODID;

public class ItemFishingRod extends ItemBaseFishingRod {
    public ItemFishingRod() {
        super(new ResourceLocation(MODID, "fishing_rod"), 0);
        setCreativeTab(Piscary.TAB);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
        }
    }
}
