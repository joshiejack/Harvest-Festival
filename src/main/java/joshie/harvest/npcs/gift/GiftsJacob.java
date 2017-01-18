package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.mining.HFMining;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsJacob extends Gifts {
    public GiftsJacob() {
        stackRegistry.register(HFFishing.FISH.getStackFromEnum(Fish.MANTARAY), Quality.AWESOME);
        stackRegistry.register(HFFishing.FISH.getStackFromEnum(Fish.ELECTRICRAY), Quality.AWESOME);
        stackRegistry.register(HFFishing.FISH.getStackFromEnum(Fish.STINGRAY), Quality.AWESOME);
        stackRegistry.register(Ore.of("fish"), Quality.GOOD);
        categoryRegistry.put(FISH, Quality.GOOD);
        categoryRegistry.put(KNOWLEDGE, Quality.DISLIKE);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        categoryRegistry.put(GEM, Quality.BAD);
        stackRegistry.register(HFMining.MATERIALS, Quality.TERRIBLE);
    }
}