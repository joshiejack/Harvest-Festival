package joshie.harvestmoon.blocks.items;

import joshie.lib.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockNPCSpawner extends ItemBlockBase {
    public ItemBlockNPCSpawner(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            default:
                return "invalid";
        }
    }
}
