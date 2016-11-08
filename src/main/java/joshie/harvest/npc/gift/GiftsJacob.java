package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.mining.HFMining;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsJacob extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == HFFishing.FISH && HFFishing.FISH.getEnumFromStack(stack) == Fish.MANTARAY) return Quality.AWESOME;
        else if (InventoryHelper.isOreName(stack, "fish")) return Quality.GOOD;
        else if (stack.getItem() == HFMining.MATERIALS) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, MINERAL, GEM)) return Quality.BAD;
        else if (registry.isGiftType(stack, KNOWLEDGE, JUNK, FLOWER)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}