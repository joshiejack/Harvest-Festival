package joshie.harvestmoon.npc.gift;

import static joshie.harvestmoon.npc.gift.Gifts.GiftQuality.AWESOME;
import static joshie.harvestmoon.npc.gift.Gifts.GiftQuality.DECENT;
import static joshie.harvestmoon.npc.gift.Gifts.GiftQuality.GOOD;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsTiberius extends Gifts {
    @Override
    public GiftQuality getValue(ItemStack stack) {
        //If we are strength two potion!
        if (stack.getItem() == Items.potionitem) {
            return stack.getItemDamage() == 8233 ? AWESOME : GOOD;
        }

        return DECENT;
    }
}
