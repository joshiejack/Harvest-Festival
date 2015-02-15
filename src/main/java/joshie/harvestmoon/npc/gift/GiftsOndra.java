package joshie.harvestmoon.npc.gift;

import static joshie.harvestmoon.npc.gift.Gifts.GiftQuality.AWESOME;
import static joshie.harvestmoon.npc.gift.Gifts.GiftQuality.DECENT;
import joshie.harvestmoon.blocks.items.ItemBlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsOndra extends Gifts {
    @Override
    public GiftQuality getValue(ItemStack stack) {
        if(stack.getItem() == Item.getItemFromBlock(Blocks.red_flower)) {
            return AWESOME;
        } else if (stack.getItem() == Item.getItemFromBlock(Blocks.yellow_flower)) {
            return AWESOME;
        }
        
        return stack.getItem() instanceof ItemBlockFlower ? AWESOME : DECENT;
    }
}
