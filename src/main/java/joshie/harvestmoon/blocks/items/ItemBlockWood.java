package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.base.ItemBlockBase;
import joshie.harvestmoon.blocks.BlockWood;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockWood extends ItemBlockBase {
    public ItemBlockWood(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BlockWood.SHIPPING:
                return "shipping";
            case BlockWood.RURAL_CHEST:
                return "chest";
            case BlockWood.OLD_RURAL_CHEST:
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
}
