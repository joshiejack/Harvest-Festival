package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsGoddess extends Gifts {
    public GiftsGoddess() {
        stackRegistry.register(Ore.of("cropStrawberry"), Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(COOKING, Quality.DISLIKE);
        categoryRegistry.put(SWEET, Quality.DISLIKE);
        categoryRegistry.put(FISH, Quality.DISLIKE);
        categoryRegistry.put(KNOWLEDGE, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        categoryRegistry.put(MEAT, Quality.BAD);
        categoryRegistry.put(GEM, Quality.BAD);
        categoryRegistry.put(PLANT, Quality.BAD);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.ALEXANDRITE), Quality.TERRIBLE);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.PINK_DIAMOND), Quality.TERRIBLE);
        stackRegistry.register(HFFishing.JUNK.getStackFromEnum(Junk.BOOT), Quality.TERRIBLE);
    }
}