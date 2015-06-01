package joshie.harvest.core.util.generic;

import net.minecraft.item.ItemBlock;

public interface IHasMetaBlock {
    public int getMetaCount();
    public Class<? extends ItemBlock> getItemClass();
}
