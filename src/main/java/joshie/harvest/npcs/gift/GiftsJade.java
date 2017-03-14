package joshie.harvest.npcs.gift;

import joshie.harvest.mining.HFMining;
import net.minecraft.init.Blocks;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsJade extends Gifts {
    public GiftsJade() {
        stackRegistry.register(Blocks.YELLOW_FLOWER, Quality.AWESOME);
        categoryRegistry.put(FLOWER, Quality.GOOD);
        categoryRegistry.put(VEGETABLE, Quality.GOOD);
        categoryRegistry.put(FRUIT, Quality.GOOD);
        categoryRegistry.put(HERB, Quality.GOOD);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        categoryRegistry.put(BUILDING, Quality.TERRIBLE);
        stackRegistry.register(HFMining.MATERIALS, Quality.TERRIBLE);
    }
}