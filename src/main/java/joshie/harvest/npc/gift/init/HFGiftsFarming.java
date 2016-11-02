package joshie.harvest.npc.gift.init;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.core.Size;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.core.util.annotations.HFLoader;

import static joshie.harvest.animals.HFAnimals.*;
import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.animals.HFAnimals.ANIMAL_PRODUCT;
import static joshie.harvest.core.HFCore.STORAGE;
import static joshie.harvest.crops.HFCrops.*;

@HFLoader(priority = 0)
public class HFGiftsFarming extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(SEEDS, FARMING);
        assignGeneric(TRAY, FARMING);
        assignGeneric(SIZED, FARMING);
        assignGeneric(TROUGH, FARMING);
        assignGeneric(STORAGE, FARMING);
        assignGeneric(TREATS, FARMING, ANIMALS);
        assignGeneric(HFAnimals.TOOLS, FARMING, ANIMALS);
        assignGeneric(SPRINKLER, FARMING, TECHNOLOGY);
        assignGeneric(CROP, FARMING, NATURE, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.SMALL), FARMING, ANIMALS, KNITTING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.MEDIUM), FARMING, ANIMALS, KNITTING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.LARGE), FARMING, ANIMALS, KNITTING, RARE);
        //Stuffs
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.SMALL), FARMING, ANIMALS, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.MEDIUM), FARMING, ANIMALS, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.LARGE), FARMING, ANIMALS, COOKING, RARE);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.SMALL), FARMING, ANIMALS, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.MEDIUM), FARMING, ANIMALS, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.LARGE), FARMING, ANIMALS, COOKING, RARE);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.SMALL), FARMING, ANIMALS, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.MEDIUM), FARMING, ANIMALS, COOKING);
        assignGeneric(ANIMAL_PRODUCT.getStack(Sizeable.MAYONNAISE, Size.LARGE), FARMING, ANIMALS, COOKING, RARE);
    }
}