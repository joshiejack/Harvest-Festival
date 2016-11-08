package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsThomas extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == Item.getItemFromBlock(Blocks.TNT) || stack.getItem() == Items.GUNPOWDER) return Quality.AWESOME;
        else if (registry.isGiftType(stack, MEAT)) return Quality.GOOD;
        else if (registry.isGiftType(stack, MONSTER)) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, MAGIC, KNOWLEDGE)) return Quality.BAD;
        else if (registry.isGiftType(stack, SWEET, FLOWER)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}