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
                return "pan.frying";
            case BlockGeneral.MIXER:
                return "mixer";
            case BlockGeneral.OVEN:
                return "oven";
            case BlockGeneral.STEAMER:
                return "steamer";
            default:
                return "invalid";
        }
    }
}
