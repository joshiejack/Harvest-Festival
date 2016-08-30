package joshie.harvest.npc.gift.init;

import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.util.HFLoader;

import static joshie.harvest.animals.HFAnimals.*;
import static joshie.harvest.api.npc.gift.GiftCategory.*;

@HFLoader(priority = 0)
public class HFGiftsSized extends HFGiftsAbstract {
    public static void remap() {
        removeItem(HFCore.SIZEABLE); //Remove the resizeables
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
