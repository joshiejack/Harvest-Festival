package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsJade extends Gifts {
    public GiftsJade() {
        stackRegistry.register(Blocks.YELLOW_FLOWER, Quality.AWESOME);
        stackRegistry.register(new ItemStack(Blocks.RED_FLOWER, 1, 2), Quality.AWESOME);
        categoryRegistry.put(FLOWER, Quality.GOOD);
        categoryRegistry.put(PLANT, Quality.GOOD);
        categoryRegistry.put(HERB, Quality.GOOD);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        stackRegistry.register(Ore.of("stone"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("logWood"), Quality.TERRIBLE);
    }
}