package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsJim extends Gifts {
    @Override
    public Quality getQuality(ItemStack stack) {
        if (stack.getItem() == Items.STICK ||
                (stack.getItem() == Item.getItemFromBlock(HFCore.FLOWERS) && HFCore.FLOWERS.getEnumFromStack(stack) == FlowerType.WEED)) return Quality.AWESOME;
        else if (HFApi.npc.getGifts().isGiftType(stack, JUNK, ANIMAL)) return Quality.GOOD;
        else if (HFApi.npc.getGifts().isGiftType(stack, GEM)) return Quality.TERRIBLE;
        else if (InventoryHelper.isOreName(stack, "fish"))return Quality.BAD;
        else if (HFApi.npc.getGifts().isGiftType(stack, MAGIC))return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}
