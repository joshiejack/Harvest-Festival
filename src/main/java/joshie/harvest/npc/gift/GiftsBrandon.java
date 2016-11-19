package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsBrandon extends Gifts {
    public GiftsBrandon() {
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.MYTHIC), Quality.AWESOME);
        categoryRegistry.put(MINERAL, Quality.GOOD);
        categoryRegistry.put(PLANT, Quality.DISLIKE);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(SWEET, Quality.DISLIKE);
        categoryRegistry.put(FRUIT, Quality.DISLIKE);
        categoryRegistry.put(FISH, Quality.BAD);
        stackRegistry.register(Ore.of("flower"), Quality.BAD);
        stackRegistry.register(Ore.of("treeSapling"), Quality.TERRIBLE);
    }
}