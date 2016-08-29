package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GiftsThomas extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Item.getItemFromBlock(Blocks.TNT)) {
            return Quality.AWESOME;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.BATTLE)) {
            return Quality.BAD;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.DANGER)) {
            return Quality.GOOD;
        }

        return Quality.DECENT;
    }
}