package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.blocks.BlockCookware;
import joshie.harvestmoon.core.util.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockCookware extends ItemBlockBase {
    public ItemBlockCookware(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BlockCookware.FRIDGE:
                return "fridge";
            case BlockCookware.KITCHEN:
                return "kitchen";
            case BlockCookware.POT:
                return "pot";
            case BlockCookware.FRYING_PAN:
                return "frying.pan";
            case BlockCookware.MIXER:
                return "mixer";
            case BlockCookware.OVEN:
                return "oven";
            case BlockCookware.STEAMER:
                return "steamer";
            case BlockCookware.FRIDGE_TOP:
                return "fridge.top";
            default:
                return "invalid";
        }
    }
}
