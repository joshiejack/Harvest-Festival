package joshie.harvest.blocks.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.blocks.BlockWood;
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
        switch (BlockWood.Woodware.SHIPPING) {
            case SHIPPING:
                return "shipping";
            case SHIPPING_2:
                return "shipping2";
            case NEST:
                return "nest";
            case TROUGH:
                return "trough";
            case TROUGH_2:
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