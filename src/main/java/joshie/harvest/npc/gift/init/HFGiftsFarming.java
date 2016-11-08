package joshie.harvest.npc.gift.init;

import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.core.util.annotations.HFLoader;

import static joshie.harvest.animals.HFAnimals.ANIMAL_PRODUCT;
import static joshie.harvest.animals.HFAnimals.TREATS;
import static joshie.harvest.api.npc.gift.GiftCategory.*;

@HFLoader(priority = 0)
public class HFGiftsFarming extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(TREATS, JUNK);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.SMALL), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.MEDIUM), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.LARGE), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.SMALL), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.MEDIUM), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.LARGE), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.SMALL), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.MEDIUM), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.LARGE), ANIMAL);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.SMALL), ANIMAL, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.MEDIUM), ANIMAL, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.LARGE), ANIMAL, COOKING);
    }

    public static void postInit() {
        for (Crop crop: joshie.harvest.api.crops.Crop.REGISTRY) {
            if (crop != Crop.NULL_CROP)
                assignGeneric(crop.getCropStack(1), crop.getFoodType() == AnimalFoodType.FRUIT ? FRUIT : crop.getFoodType() == AnimalFoodType.VEGETABLE ? VEGETABLE : PLANT);
        }
    }
}