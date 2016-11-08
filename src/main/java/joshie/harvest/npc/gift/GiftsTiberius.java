package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.AWESOME;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.GOOD;

public class GiftsTiberius extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() instanceof ItemPotion) {
            for (PotionEffect effect: PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == MobEffects.REGENERATION) return AWESOME;
            }

            return GOOD;
        } else if (registry.isGiftType(stack, MAGIC, KNOWLEDGE, MONSTER)) return GOOD;
        else if (registry.isGiftType(stack, FLOWER)) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, SWEET)) return Quality.BAD;
        else return super.getQuality(stack);
    }
}
