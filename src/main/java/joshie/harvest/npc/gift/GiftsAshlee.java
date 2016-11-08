package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsAshlee extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (InventoryHelper.isOreName(stack, "gemDiamond", "gemEmerald", "gemRuby", "gemAmethyst", "gemTopaz")) return Quality.AWESOME;
        else if (registry.isGiftType(stack, ANIMAL, FRUIT, VEGETABLE)) return Quality.GOOD;
        else if (registry.isGiftType(stack, MEAT)) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, SWEET)) return Quality.BAD;
        else if (registry.isGiftType(stack, JUNK)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}
