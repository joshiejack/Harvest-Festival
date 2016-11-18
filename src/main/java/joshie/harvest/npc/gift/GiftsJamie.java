package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.GOOD;

@SuppressWarnings("unused")
public class GiftsJamie extends Gifts {
    public GiftsJamie() {
        categoryRegistry.put(MONSTER, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(COOKING, Quality.DECENT);
        categoryRegistry.put(SWEET, Quality.DECENT);
    }


    @Override
    public Quality getQuality(ItemStack stack) {
        long sell = HFApi.shipping.getSellValue(stack);
        Quality quality = super.getQuality(stack);
        if (quality == GOOD) {
            return quality;
        } else {
            if (sell <= 1) return Quality.TERRIBLE;
            if (sell <= 80) return Quality.BAD;
            if (sell <= 150) return Quality.DISLIKE;
            if (sell <= 300) return Quality.DECENT;
            return sell >= 1000 ? Quality.AWESOME : GOOD;
        }
    }
}