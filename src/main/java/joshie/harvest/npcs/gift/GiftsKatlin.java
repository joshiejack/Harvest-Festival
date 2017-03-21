package joshie.harvest.npcs.gift;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.core.Size;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@SuppressWarnings("unused")
public class GiftsKatlin extends Gifts {
    public GiftsKatlin() {
        stackRegistry.register(Ore.of("string"), Quality.AWESOME);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.SMALL), Quality.AWESOME);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.MEDIUM), Quality.AWESOME);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.LARGE), Quality.AWESOME);
        categoryRegistry.put(WOOL, Quality.GOOD);
        categoryRegistry.put(HERB, Quality.GOOD);
        categoryRegistry.put(MUSHROOM, Quality.GOOD);
        categoryRegistry.put(MAGIC, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        stackRegistry.register(Ore.of("dustRedstone"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("gemQuartz"), Quality.TERRIBLE);
    }
}