package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsJade extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == Item.getItemFromBlock(Blocks.YELLOW_FLOWER)) return Quality.AWESOME;
        else if (registry.isGiftType(stack, FLOWER, VEGETABLE, FRUIT)) return Quality.GOOD;
        else if (registry.isGiftType(stack, BUILDING)) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, MINERAL)) return Quality.BAD;
        else if (registry.isGiftType(stack, JUNK)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}