package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;

@Deprecated //TODO: Remove in 0.7+
public interface IAltItem {
    ItemStack getAlternativeWhenCooking(ItemStack stack);
}
