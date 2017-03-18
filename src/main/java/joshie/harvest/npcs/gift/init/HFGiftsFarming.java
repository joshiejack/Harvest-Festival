package joshie.harvest.npcs.gift.init;

import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.core.util.annotations.HFLoader;

import static joshie.harvest.animals.HFAnimals.*;
import static joshie.harvest.api.npc.gift.GiftCategory.ANIMAL;
import static joshie.harvest.api.npc.gift.GiftCategory.*;

@HFLoader(priority = 0)
@SuppressWarnings("unused")
public class HFGiftsFarming extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(TREATS, JUNK);
        registerAllSizes(Sizeable.WOOL, ANIMAL);
        registerAllSizes(Sizeable.EGG, ANIMAL);
        registerAllSizes(Sizeable.MILK, ANIMAL);
        registerAllSizes(Sizeable.MAYONNAISE, ANIMAL, COOKING);
    }

    private static void registerAllSizes(Sizeable sizeable, GiftCategory... categories) {
        assignGeneric(ANIMAL_PRODUCT.getStack(sizeable, Size.SMALL), categories);
        assignGeneric(ANIMAL_PRODUCT.getStack(sizeable, Size.MEDIUM), categories);
        assignGeneric(ANIMAL_PRODUCT.getStack(sizeable, Size.LARGE), categories);
        assignGeneric(GOLDEN_PRODUCT.getStackFromEnum(sizeable), categories);
    }

    public static void postInit() {
        Crop.REGISTRY.values().stream().filter(crop -> crop != Crop.NULL_CROP)
                .forEachOrdered(crop -> assignGeneric(crop.getCropStack(1),
                        crop.getFoodType() == AnimalFoodType.FRUIT ? FRUIT :
                                crop.getFoodType() == AnimalFoodType.VEGETABLE ? VEGETABLE : PLANT));
    }
}