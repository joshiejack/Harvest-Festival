package joshie.harvest.npc.gift.init;

import joshie.harvest.core.util.annotations.HFLoader;

import static joshie.harvest.api.npc.gift.GiftCategory.CONSTRUCTION;
import static joshie.harvest.buildings.HFBuildings.BLUEPRINTS;

@HFLoader(priority = 0)
public class HFGiftsTown extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(BLUEPRINTS, CONSTRUCTION);
    }
}