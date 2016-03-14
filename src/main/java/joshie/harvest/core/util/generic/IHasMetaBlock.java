package joshie.harvest.core.util.generic;

import net.minecraft.item.ItemBlock;

public interface IHasMetaBlock {
    public Class<? extends ItemBlock> getItemClass();
}
