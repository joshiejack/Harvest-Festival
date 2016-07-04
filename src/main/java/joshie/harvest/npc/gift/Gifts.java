package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.IGiftHandler;
import net.minecraft.item.ItemStack;

public class Gifts implements IGiftHandler {
    public static final Gifts INSTANCE = new Gifts();

    public Quality getQuality(ItemStack stack) {
        return Quality.DECENT;
    }
}