package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.*;

public class GiftsTiberius extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        //If we are strength two potion!
        if (stack.getItem() instanceof ItemPotion) {
            for (PotionEffect effect: PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == MobEffects.REGENERATION) return AWESOME;
            }

            return GOOD;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.BATTLE)) {
            return Quality.GOOD;
        }

        if (HFApi.npc.getGifts().isGiftType(stack, GiftCategory.GIRLY)) {
            return Quality.BAD;
        }

        return DECENT;
    }
}
