package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.AWESOME;
import static joshie.harvest.api.npc.gift.IGiftHandler.Quality.GOOD;

public class GiftsKatlin extends Gifts {
    private static final Item WOOL = Item.getItemFromBlock(Blocks.WOOL);
    private static final Item CARPET = Item.getItemFromBlock(Blocks.CARPET);

    static boolean isWoolLikeItem(ItemStack stack) {
        Item item = stack.getItem();
        return ToolHelper.isWool(stack) || item == WOOL || item == CARPET || InventoryHelper.isOreName(stack, "string") || item == Items.BANNER;
    }

    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (isWoolLikeItem(stack)) return AWESOME;
        else if (registry.isGiftType(stack, COOKING)) return GOOD;
        else if (InventoryHelper.isOreName(stack, "dustRedstone", "gemQuartz")) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, MINERAL)) return Quality.BAD;
        else if (registry.isGiftType(stack, MAGIC)) return Quality.DISLIKE;
        else return super.getQuality(stack);
    }
}