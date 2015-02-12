package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.base.ItemBlockBase;
import joshie.harvestmoon.blocks.BlockStone;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockStone extends ItemBlockBase {
    public ItemBlockStone(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BlockStone.CAVE_WALL:
                return "mine_wall";
            default:
                return "invalid";
        }
    }
}
