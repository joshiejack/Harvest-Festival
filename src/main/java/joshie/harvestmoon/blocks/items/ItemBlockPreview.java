package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockPreview extends ItemBlockBase {
    public ItemBlockPreview(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        return "name";
    }
}
