package joshie.harvest.blocks.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.core.util.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockCookware extends ItemBlockBase implements ICreativeSorted {
    public ItemBlockCookware(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (BlockCookware.Cookware.POT) {
            case FRIDGE_TOP:
                return "fridge.top";
            case FRIDGE:
                return "fridge";
            case COUNTER:
                return "COUNTER";
            case POT:
                return "pot";
            case FRYING_PAN:
                return "frying.pan";
            case MIXER:
                return "mixer";
            case OVEN:
                return "oven";
            default:
                return "invalid";
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 99;
    }
}