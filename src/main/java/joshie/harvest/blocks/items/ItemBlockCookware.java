package joshie.harvest.blocks.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.util.base.ItemBlockHFBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockCookware extends ItemBlockHFBase implements ICreativeSorted {
    public ItemBlockCookware(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        return HFBlocks.COOKWARE.getEnumFromMeta(stack.getItemDamage()).name().toLowerCase().replace("_", "");
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 99;
    }
}