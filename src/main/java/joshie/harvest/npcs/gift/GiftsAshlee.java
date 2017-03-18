package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsAshlee extends Gifts {
    public GiftsAshlee() {
        stackRegistry.register(Ore.of("gemDiamond"), Quality.AWESOME);
        stackRegistry.register(Ore.of("gemEmerald"), Quality.AWESOME);
        stackRegistry.register(Ore.of("gemRuby"), Quality.AWESOME);
        stackRegistry.register(Ore.of("gemAmethyst"), Quality.AWESOME);
        stackRegistry.register(Ore.of("gemTopaz"), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.ALEXANDRITE), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.PINK_DIAMOND), Quality.AWESOME);
        categoryRegistry.put(HERB, Quality.GOOD);
        categoryRegistry.put(ANIMAL, Quality.GOOD);
        categoryRegistry.put(FRUIT, Quality.GOOD);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        categoryRegistry.put(SWEET, Quality.BAD);
        categoryRegistry.put(MEAT, Quality.TERRIBLE);
    }
}
