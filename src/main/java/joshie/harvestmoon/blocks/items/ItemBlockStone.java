package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.blocks.BlockStone;
import joshie.harvestmoon.core.util.base.ItemBlockBase;
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
