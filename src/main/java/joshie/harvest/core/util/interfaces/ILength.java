package joshie.harvest.core.util.interfaces;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ILength {
    double getLengthFromSizeOfFish(@Nonnull ItemStack stack, int size);
}