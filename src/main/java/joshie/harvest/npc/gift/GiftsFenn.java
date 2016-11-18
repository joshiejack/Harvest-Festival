package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsFenn extends Gifts {
    public GiftsFenn() {
        stackRegistry.register(Ore.of("vine"), Quality.AWESOME);
        stackRegistry.register(Ore.of("treeLeaves"), Quality.AWESOME);
        categoryRegistry.put(PLANT, Quality.GOOD);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.ALEXANDRITE), Quality.DECENT);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.PINK_DIAMOND), Quality.DECENT);
        categoryRegistry.put(GEM, Quality.DISLIKE);
        categoryRegistry.put(MONSTER, Quality.DISLIKE);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        categoryRegistry.put(SWEET, Quality.BAD);
        categoryRegistry.put(COOKING, Quality.BAD);
        stackRegistry.register(Ore.of("cropSweetPotato"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropBeetroot"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropGrape"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropPeach"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropTurnip"), Quality.TERRIBLE);
    }
}
