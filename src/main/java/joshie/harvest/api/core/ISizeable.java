package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

/** Items that implement this, come in small, medium and large **/
public interface ISizeable {
    public Size getSize(ItemStack stack);
    
    public static enum Size {
        SMALL, MEDIUM, LARGE;
    }
}
