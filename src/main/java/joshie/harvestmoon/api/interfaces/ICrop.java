package joshie.harvestmoon.api.interfaces;

import net.minecraft.item.ItemStack;

public interface ICrop {
    /** Returns this crop in seed form, with default stats **/
    public ItemStack getSeedStack();

    /** Return this crop in item form, with default stats **/
    public ItemStack getCropStack();
}
