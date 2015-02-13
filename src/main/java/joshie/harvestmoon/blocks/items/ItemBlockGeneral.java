package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.base.ItemBlockBase;
import joshie.harvestmoon.blocks.BlockGeneral;
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
            case BlockGeneral.BAKING_GLASS:
                return "baking";
            case BlockGeneral.CHOPPING_BOARD:
                return "chopping_board";
            case BlockGeneral.MIXING_BOWL:
                return "mixing_bowl";
            case BlockGeneral.NEST:
                return "nest";
            case BlockGeneral.TROUGH:
                return "trough";
            case BlockGeneral.DISPLAY:
                return "display";
            default:
                return "invalid";
        }
    }
}
