package joshie.harvest.npcs.gift;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsThomas extends Gifts {
    public GiftsThomas() {
        stackRegistry.register(Blocks.TNT, Quality.AWESOME);
        stackRegistry.register(Items.GUNPOWDER, Quality.AWESOME);
        categoryRegistry.put(MEAT, Quality.GOOD);
        categoryRegistry.put(SWEET, Quality.DISLIKE);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(MAGIC, Quality.BAD);
        categoryRegistry.put(KNOWLEDGE, Quality.BAD);
        categoryRegistry.put(MONSTER, Quality.TERRIBLE);
        stackRegistry.register(Items.POTIONITEM, Quality.TERRIBLE);
        stackRegistry.register(Items.LINGERING_POTION, Quality.TERRIBLE);
        stackRegistry.register(Items.SPLASH_POTION, Quality.TERRIBLE);
        stackRegistry.register(Items.EXPERIENCE_BOTTLE, Quality.TERRIBLE);
        stackRegistry.register(Items.ENCHANTED_BOOK, Quality.TERRIBLE);
    }
}