package joshie.harvest.blocks.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockWood extends ItemBlockBase implements ICreativeSorted {
    public ItemBlockWood(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        return HFBlocks.WOOD_MACHINES.getEnumFromMeta(stack.getItemDamage()).name().toLowerCase().replace("_", "");
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}