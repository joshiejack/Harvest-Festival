package joshie.harvest.npc.gift.init;

import joshie.harvest.core.util.annotations.HFLoader;

import static joshie.harvest.api.npc.gift.GiftCategory.COOKING;
import static joshie.harvest.cooking.HFCooking.INGREDIENTS;
import static joshie.harvest.cooking.HFCooking.MEAL;

@HFLoader(priority = 0)
public class HFGiftsCooking extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(MEAL, COOKING);
        assignGeneric(INGREDIENTS, COOKING);
    }
}