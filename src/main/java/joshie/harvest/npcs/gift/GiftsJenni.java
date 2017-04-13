package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsJenni extends Gifts {
    public GiftsJenni() {
        stackRegistry.register(Items.CARROT_ON_A_STICK, Quality.AWESOME);
        stackRegistry.register(Ore.of("cropCarrot"), Quality.AWESOME);
        stackRegistry.register(Items.GOLDEN_CARROT, Quality.AWESOME);
        categoryRegistry.put(ART, Quality.GOOD);
        stackRegistry.register(Items.RABBIT_FOOT, Quality.GOOD);
        categoryRegistry.put(WOOL, Quality.GOOD);
        categoryRegistry.put(VEGETABLE, Quality.GOOD);
        categoryRegistry.put(MONEY, Quality.DECENT);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        categoryRegistry.put(BUILDING, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        stackRegistry.register(Items.FLINT, Quality.TERRIBLE);
        stackRegistry.register(Ore.of("ingotIron"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("ingotGold"), Quality.TERRIBLE);
    }
}