package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsDaniel extends Gifts {
    public GiftsDaniel() {
        stackRegistry.register(Ore.of("enderpearl"), Quality.AWESOME);
        stackRegistry.register(Items.ENDER_EYE, Quality.AWESOME);
        categoryRegistry.put(MINERAL, Quality.GOOD);
        categoryRegistry.put(MONSTER, Quality.DECENT);
        categoryRegistry.put(MAGIC, Quality.DISLIKE);
        stackRegistry.register(new ItemStack(Items.COOKED_FISH, 1, 0), Quality.BAD);
        stackRegistry.register(new ItemStack(Items.COOKED_FISH, 1, 1), Quality.BAD);
        categoryRegistry.put(FISH, Quality.TERRIBLE);
    }
}