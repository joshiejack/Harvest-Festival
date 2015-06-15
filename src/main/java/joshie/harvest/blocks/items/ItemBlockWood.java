package joshie.harvest.blocks.items;

import joshie.harvest.blocks.BlockWood;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.ICreativeSorted;
import joshie.harvest.core.util.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockWood extends ItemBlockBase implements ICreativeSorted {
    public ItemBlockWood(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BlockWood.SHIPPING:
                return "shipping";
            case BlockWood.SHIPPING_2:
                return "shipping2";
            case BlockWood.NEST:
                return "nest";
            case BlockWood.TROUGH:
                return "trough";
            case BlockWood.TROUGH_2:
                return "trough2";
            default:
                return "invalid";
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }
}
