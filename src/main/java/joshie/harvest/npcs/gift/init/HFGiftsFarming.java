package joshie.harvest.npcs.gift.init;

import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.core.util.annotations.HFLoader;
import org.apache.commons.lang3.text.WordUtils;

import static joshie.harvest.animals.HFAnimals.*;
import static joshie.harvest.api.npc.gift.GiftCategory.*;

@HFLoader(priority = 0)
@SuppressWarnings("unused")
public class HFGiftsFarming extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(TREATS, JUNK);
        registerAllSizes(Sizeable.WOOL, WOOL);
        registerAllSizes(Sizeable.MILK, MILK);
        registerAllSizes(Sizeable.MAYONNAISE, COOKING);
        assignGeneric(GOLDEN_PRODUCT.getStackFromEnum(Sizeable.EGG), EGG);
    }

    private static void registerAllSizes(Sizeable sizeable, GiftCategory category) {
        assignGeneric(ANIMAL_PRODUCT.getStack(sizeable, Size.SMALL), category);
        assignGeneric(ANIMAL_PRODUCT.getStack(sizeable, Size.MEDIUM), category);
        assignGeneric(ANIMAL_PRODUCT.getStack(sizeable, Size.LARGE), category);
        assignGeneric(GOLDEN_PRODUCT.getStackFromEnum(sizeable), category);
    }

    public static void postInit() {
        Crop.REGISTRY.values().stream().filter(crop -> crop != Crop.NULL_CROP)
                .forEachOrdered(crop -> assignGeneric(Ore.of("crop" + WordUtils.capitalizeFully(crop.getResource().getResourcePath(), '_').replace("_", "")),
                        crop.getFoodType() == AnimalFoodType.FRUIT ? FRUIT :
                                crop.getFoodType() == AnimalFoodType.VEGETABLE ? VEGETABLE : PLANT));
    }
}