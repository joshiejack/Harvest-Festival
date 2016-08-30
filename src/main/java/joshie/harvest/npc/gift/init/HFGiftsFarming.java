package joshie.harvest.npc.gift.init;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.util.HFLoader;

import static joshie.harvest.animals.HFAnimals.*;
import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.api.npc.gift.GiftCategory.TOOLS;
import static joshie.harvest.core.HFCore.STORAGE;
import static joshie.harvest.crops.HFCrops.*;
import static joshie.harvest.tools.HFTools.*;

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
        assignGeneric(SICKLE.getStack(ToolTier.BASIC), CHEAP, TOOLS, FARMING);
        assignGeneric(HOE.getStack(ToolTier.BASIC), CHEAP, TOOLS, FARMING);
        assignGeneric(WATERING_CAN.getStack(ToolTier.BASIC), CHEAP, TOOLS, FARMING);
        assignGeneric(SICKLE.getStack(ToolTier.COPPER), CHEAP, TOOLS, FARMING);
        assignGeneric(HOE.getStack(ToolTier.COPPER), CHEAP, TOOLS, FARMING);
        assignGeneric(WATERING_CAN.getStack(ToolTier.COPPER), CHEAP, TOOLS, FARMING);
        assignGeneric(SICKLE.getStack(ToolTier.SILVER), RARE, TOOLS, FARMING);
        assignGeneric(HOE.getStack(ToolTier.SILVER), RARE, TOOLS, FARMING);
        assignGeneric(WATERING_CAN.getStack(ToolTier.SILVER), RARE, TOOLS, FARMING);
        assignGeneric(SICKLE.getStack(ToolTier.GOLD), RARE, TOOLS, FARMING);
        assignGeneric(HOE.getStack(ToolTier.GOLD), RARE, TOOLS, FARMING);
        assignGeneric(WATERING_CAN.getStack(ToolTier.GOLD), RARE, TOOLS, FARMING);
        assignGeneric(SICKLE.getStack(ToolTier.MYSTRIL), RARE, TOOLS, FARMING);
        assignGeneric(HOE.getStack(ToolTier.MYSTRIL), RARE, TOOLS, FARMING);
        assignGeneric(WATERING_CAN.getStack(ToolTier.MYSTRIL), RARE, TOOLS, FARMING);
        assignGeneric(SICKLE.getStack(ToolTier.CURSED), RARE, TOOLS, FARMING, SCARY);
        assignGeneric(HOE.getStack(ToolTier.CURSED), RARE, TOOLS, FARMING, SCARY);
        assignGeneric(WATERING_CAN.getStack(ToolTier.CURSED), RARE, TOOLS, FARMING, SCARY);
        assignGeneric(SICKLE.getStack(ToolTier.BLESSED), RARE, TOOLS, FARMING, PRETTY);
        assignGeneric(HOE.getStack(ToolTier.BLESSED), RARE, TOOLS, FARMING, PRETTY);
        assignGeneric(WATERING_CAN.getStack(ToolTier.BLESSED), RARE, TOOLS, FARMING, PRETTY);
        assignGeneric(SICKLE.getStack(ToolTier.MYTHIC), RARE, TOOLS, FARMING);
        assignGeneric(HOE.getStack(ToolTier.MYTHIC), RARE, TOOLS, FARMING);
        assignGeneric(WATERING_CAN.getStack(ToolTier.MYTHIC), RARE, TOOLS, FARMING);
    }
}