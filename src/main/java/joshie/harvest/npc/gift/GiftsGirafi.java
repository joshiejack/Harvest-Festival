package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsGirafi extends Gifts {
    public GiftsGirafi() {
        stackRegistry.register(Ore.of("cropPotato"), Quality.AWESOME);
        stackRegistry.register(Items.BAKED_POTATO, Quality.AWESOME);
        categoryRegistry.put(ANIMAL, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(SWEET, Quality.BAD);
        registerWoolLikeItems(Quality.TERRIBLE);
    }
}