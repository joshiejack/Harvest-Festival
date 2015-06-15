package joshie.harvest.blocks.items;

import joshie.harvest.core.util.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockStone extends ItemBlockBase {
    public ItemBlockStone(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        return "mine_wall";
    }
}
