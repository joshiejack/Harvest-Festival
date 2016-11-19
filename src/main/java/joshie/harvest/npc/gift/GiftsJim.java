package joshie.harvest.npc.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsJim extends Gifts {
    public GiftsJim() {
        stackRegistry.register(Items.STICK, Quality.AWESOME);
        stackRegistry.register(HFCore.FLOWERS.getStackFromEnum(FlowerType.WEED), Quality.AWESOME);
        categoryRegistry.put(JUNK, Quality.GOOD);
        categoryRegistry.put(ANIMAL, Quality.GOOD);
        categoryRegistry.put(MAGIC, Quality.DISLIKE);
        stackRegistry.register(Ore.of("fish"), Quality.BAD);
        categoryRegistry.put(GEM, Quality.TERRIBLE);
    }
}
