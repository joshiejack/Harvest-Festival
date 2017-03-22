package joshie.harvest.npcs.gift;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsJim extends Gifts {
    public GiftsJim() {
        stackRegistry.register(Items.STICK, Quality.AWESOME);
        stackRegistry.register(HFCore.FLOWERS.getStackFromEnum(FlowerType.WEED), Quality.AWESOME);
        categoryRegistry.put(JUNK, Quality.GOOD);
        categoryRegistry.put(MILK, Quality.GOOD);
        categoryRegistry.put(WOOL, Quality.GOOD);
        categoryRegistry.put(MEAT, Quality.GOOD);
        stackRegistry.register(Items.RABBIT_FOOT, Quality.GOOD);
        categoryRegistry.put(MAGIC, Quality.DISLIKE);
        categoryRegistry.put(EGG, Quality.DISLIKE);
        categoryRegistry.put(FISH, Quality.BAD);
        categoryRegistry.put(GEM, Quality.BAD);
        stackRegistry.register(Items.EMERALD, Quality.TERRIBLE);
        stackRegistry.register(Items.DIAMOND, Quality.TERRIBLE);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.ADAMANTITE), Quality.TERRIBLE);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.PINK_DIAMOND), Quality.TERRIBLE);
    }
}
