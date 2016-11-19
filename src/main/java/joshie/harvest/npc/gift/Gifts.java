package joshie.harvest.npc.gift;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.core.util.holders.HolderRegistry;
import joshie.harvest.npc.NPCRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.EnumMap;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class Gifts implements IGiftHandler {
    final HolderRegistry<Quality> stackRegistry = new HolderRegistry<>();
    final EnumMap<GiftCategory, Quality> categoryRegistry = new EnumMap<>(GiftCategory.class);

    public Gifts() {
        categoryRegistry.put(GEM, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.GOOD);
        categoryRegistry.put(COOKING, Quality.GOOD);
        categoryRegistry.put(SWEET, Quality.GOOD);
        categoryRegistry.put(MEAT, Quality.DECENT);
        categoryRegistry.put(VEGETABLE, Quality.DECENT);
        categoryRegistry.put(FRUIT, Quality.DECENT);
        categoryRegistry.put(ANIMAL, Quality.DECENT);
        categoryRegistry.put(KNOWLEDGE, Quality.DECENT);
        categoryRegistry.put(MAGIC, Quality.DECENT);
        categoryRegistry.put(FISH, Quality.DISLIKE);
        categoryRegistry.put(PLANT, Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        categoryRegistry.put(BUILDING, Quality.BAD);
        categoryRegistry.put(MONSTER, Quality.BAD);
        categoryRegistry.put(JUNK, Quality.BAD);
    }

    protected void registerWoolLikeItems(Quality quality) {
        stackRegistry.register(Blocks.WOOL, quality);
        stackRegistry.register(Blocks.CARPET, quality);
        stackRegistry.register(Items.BANNER, quality);
        stackRegistry.register(Ore.of("string"), quality);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.SMALL), quality);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.MEDIUM), quality);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.LARGE), quality);
    }


    public Quality getQuality(ItemStack stack) {
        Quality itemQuality = stackRegistry.getValueOf(stack);
        if (itemQuality != null) return itemQuality;

        Quality lowest = Quality.AWESOME;
        GiftCategory[] categories = NPCRegistry.INSTANCE.getGifts().getRegistry().getValueOf(stack);
        if (categories == null) return Quality.DECENT;
        for (GiftCategory category: categories) {
            Quality quality = categoryRegistry.get(category);
            if (quality.getRelationPoints() < lowest.getRelationPoints()) {
                lowest = quality;
            }
        }

        return lowest;
    }
}