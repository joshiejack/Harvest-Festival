package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftHandler;
import net.minecraft.item.ItemStack;

public class Gifts implements IGiftHandler {
    public Quality getQuality(ItemStack stack) {
        return Quality.DECENT;
    }

}