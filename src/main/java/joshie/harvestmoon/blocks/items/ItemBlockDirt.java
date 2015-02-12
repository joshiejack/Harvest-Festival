package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockDirt extends ItemBlockBase {
    public ItemBlockDirt(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        return "mine_floor";
    }
}
