package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsJamie extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        long sell = HFApi.shipping.getSellValue(stack);
        if (sell >= 5000) return Quality.AWESOME;
        else if (registry.isGiftType(stack, MONSTER) || sell >= 3000) return Quality.GOOD;
        else if (registry.isGiftType(stack, SWEET, FLOWER, JUNK, PLANT, KNOWLEDGE) || (sell > 0 && sell < 1000)) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, COOKING, ANIMAL, MAGIC) || (sell > 0 && sell < 2000)) return Quality.BAD;
        else if ((sell > 0 && sell < 3000)) return Quality.DISLIKE;
        return super.getQuality(stack);
    }
}