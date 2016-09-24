package joshie.harvest.npc.gift.init;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.util.HFLoader;

import static joshie.harvest.animals.HFAnimals.*;
import static joshie.harvest.api.npc.gift.GiftCategory.*;
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
        assignGeneric(WOOL.getStack(Size.SMALL), FARMING, ANIMALS, KNITTING);
        assignGeneric(WOOL.getStack(Size.MEDIUM), FARMING, ANIMALS, KNITTING);
        assignGeneric(WOOL.getStack(Size.LARGE), FARMING, ANIMALS, KNITTING, RARE);
        //Stuffs
        assignGeneric(EGG.getStack(Size.SMALL), FARMING, ANIMALS, COOKING);
        assignGeneric(EGG.getStack(Size.MEDIUM), FARMING, ANIMALS, COOKING);
        assignGeneric(EGG.getStack(Size.LARGE), FARMING, ANIMALS, COOKING, RARE);
        assignGeneric(MILK.getStack(Size.SMALL), FARMING, ANIMALS, COOKING);
        assignGeneric(MILK.getStack(Size.MEDIUM), FARMING, ANIMALS, COOKING);
        assignGeneric(MILK.getStack(Size.LARGE), FARMING, ANIMALS, COOKING, RARE);
        assignGeneric(MAYONNAISE.getStack(Size.SMALL), FARMING, ANIMALS, COOKING);
        assignGeneric(MAYONNAISE.getStack(Size.MEDIUM), FARMING, ANIMALS, COOKING);
        assignGeneric(MAYONNAISE.getStack(Size.LARGE), FARMING, ANIMALS, COOKING, RARE);
    }
}