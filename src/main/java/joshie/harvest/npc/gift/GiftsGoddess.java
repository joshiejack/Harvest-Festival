package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsGoddess extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) { //No awesome item, good is best
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (InventoryHelper.isOreName(stack, "cropStrawberry")) return Quality.GOOD;
        else if (stack.getItem() == HFFishing.JUNK && HFFishing.JUNK.getEnumFromStack(stack) == Junk.BOOT) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, MINERAL, KNOWLEDGE, MEAT, GEM, PLANT)) return Quality.BAD;
        else if (registry.isGiftType(stack, COOKING, SWEET, FISH, KNOWLEDGE)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}