package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.blocks.BlockGeneral;
import joshie.lib.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockGeneral extends ItemBlockBase {
    public ItemBlockGeneral(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BlockGeneral.SHIPPING:
                return "shipping";
            case BlockGeneral.FRIDGE:
                return "fridge";
            case BlockGeneral.KITCHEN:
                return "kitchen";
            case BlockGeneral.POT:
                return "pot";
            case BlockGeneral.FRYING_PAN:
                return "frying.pan";
            case BlockGeneral.MIXER:
                return "mixer";
            case BlockGeneral.OVEN:
                return "oven";
            case BlockGeneral.STEAMER:
                return "steamer";
            case BlockGeneral.FRIDGE_TOP:
                return "fridge.top";
            case BlockGeneral.RURAL_CHEST:
                return "chest";
            default:
                return "invalid";
        }
    }
}
