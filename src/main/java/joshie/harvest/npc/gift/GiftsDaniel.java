package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsDaniel extends Gifts {
    public GiftsDaniel() {
        stackRegistry.register(Ore.of("enderpearl"), Quality.AWESOME);
        stackRegistry.register(Items.ENDER_EYE, Quality.AWESOME);
        categoryRegistry.put(MINERAL, Quality.GOOD);
        categoryRegistry.put(MONSTER, Quality.DECENT);
        categoryRegistry.put(MAGIC, Quality.DISLIKE);
        categoryRegistry.put(FISH, Quality.BAD);
        stackRegistry.register(Ore.of("fish"), Quality.TERRIBLE);
    }
}