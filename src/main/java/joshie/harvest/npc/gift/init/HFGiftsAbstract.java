package joshie.harvest.npc.gift.init;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.GiftCategory;
import net.minecraft.item.Item;

public class HFGiftsAbstract {
    public static void removeItem(Item item) {
        HFApi.npc.getGifts().removeItem(item);
    }

    public static void assignGeneric(Object object, GiftCategory... categories) {
        HFApi.npc.getGifts().assignGeneric(object, categories);
    }
}
