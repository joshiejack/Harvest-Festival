package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsCloe extends Gifts {
    public GiftsCloe() {
        stackRegistry.register(Ore.of("bone"), Quality.AWESOME);
        stackRegistry.register(Items.SKULL, Quality.AWESOME);
        stackRegistry.register(Items.BOOK, Quality.AWESOME);
        stackRegistry.register(Items.ENCHANTED_BOOK, Quality.AWESOME);
        stackRegistry.register(Items.WRITABLE_BOOK, Quality.AWESOME);
        stackRegistry.register(Items.WRITTEN_BOOK, Quality.AWESOME);
        categoryRegistry.put(MONSTER, Quality.GOOD);
        categoryRegistry.put(KNOWLEDGE, Quality.GOOD);
        categoryRegistry.put(VEGETABLE, Quality.DISLIKE);
        categoryRegistry.put(HERB, Quality.DISLIKE);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(MUSHROOM, Quality.BAD);
        stackRegistry.register(Items.PUMPKIN_PIE, Quality.BAD);
        stackRegistry.register(Ore.of("cropCarrot"), Quality.BAD);
        stackRegistry.register(Ore.of("cropPumpkin"), Quality.BAD);
        stackRegistry.register(Ore.of("cropCorn"), Quality.BAD);
        stackRegistry.register(Ore.of("cropOrange"), Quality.BAD);
        stackRegistry.register(Ore.of("cropPineapple"), Quality.BAD);
        stackRegistry.register(Ore.of("cropStrawberry"), Quality.BAD);
        stackRegistry.register(Ore.of("cropSweetPotato"), Quality.BAD);
        stackRegistry.register(Ore.of("cropTomato"), Quality.BAD);
        stackRegistry.register(Ore.of("cropCabbage"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropGreenPepper"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropSpinach"), Quality.TERRIBLE);
    }
}