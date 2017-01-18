package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;

import static joshie.harvest.api.npc.gift.GiftCategory.MAGIC;
import static joshie.harvest.api.npc.gift.GiftCategory.MINERAL;

@SuppressWarnings("unused")
public class GiftsKatlin extends Gifts {
    public GiftsKatlin() {
        registerWoolLikeItems(Quality.AWESOME);
        categoryRegistry.put(MAGIC, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        stackRegistry.register(Ore.of("dustRedstone"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("gemQuartz"), Quality.TERRIBLE);
    }
}