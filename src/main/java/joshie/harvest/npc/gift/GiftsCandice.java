package joshie.harvest.npc.gift;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GiftsCandice extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.MILK_BUCKET || stack.getItem() == HFAnimals.MILK) {
            return Quality.AWESOME;
        }

        if (GiftRegistry.is(stack, GiftCategory.ANIMALS)) {
            return Quality.GOOD;
        }

        if (GiftRegistry.is(stack, GiftCategory.COOKING)) {
            return Quality.BAD;
        }

        return Quality.DECENT;
    }
}
