package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.api.core.ICreativeSorted;
import joshie.harvestmoon.blocks.BlockWood;
import joshie.harvestmoon.core.lib.CreativeSort;
import joshie.harvestmoon.core.util.base.ItemBlockBase;
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
            case BlockWood.RURAL_CHEST:
                return "chest";
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
