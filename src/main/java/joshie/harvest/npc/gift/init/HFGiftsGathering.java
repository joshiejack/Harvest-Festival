package joshie.harvest.npc.gift.init;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.gathering.block.BlockRock.Rock;
import joshie.harvest.gathering.block.BlockWood.Wood;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.core.HFCore.FLOWERS;
import static joshie.harvest.gathering.HFGathering.ROCK;
import static joshie.harvest.gathering.HFGathering.WOOD;
import static joshie.harvest.tools.HFTools.AXE;
import static joshie.harvest.tools.HFTools.HAMMER;

@HFLoader(priority = 0)
public class HFGiftsGathering extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.WEED), CHEAP, NATURE, GATHERING);
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.GODDESS), NATURE, PRETTY, GIRLY);
        assignGeneric(WOOD.getStackFromEnum(Wood.BRANCH_SMALL), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(WOOD.getStackFromEnum(Wood.BRANCH_MEDIUM), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(WOOD.getStackFromEnum(Wood.BRANCH_LARGE), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(WOOD.getStackFromEnum(Wood.STUMP_SMALL), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(WOOD.getStackFromEnum(Wood.STUMP_MEDIUM), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(WOOD.getStackFromEnum(Wood.STUMP_LARGE), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(ROCK.getStackFromEnum(Rock.STONE_SMALL), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(ROCK.getStackFromEnum(Rock.STONE_MEDIUM), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(ROCK.getStackFromEnum(Rock.STONE_LARGE), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(ROCK.getStackFromEnum(Rock.BOULDER_SMALL), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(ROCK.getStackFromEnum(Rock.BOULDER_MEDIUM), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(ROCK.getStackFromEnum(Rock.BOULDER_LARGE), CHEAP, NATURE, CONSTRUCTION, GATHERING);
        assignGeneric(AXE.getStack(ToolTier.BASIC), CHEAP, TOOLS, GATHERING);
        assignGeneric(HAMMER.getStack(ToolTier.BASIC), CHEAP, TOOLS, MINING);
        assignGeneric(AXE.getStack(ToolTier.COPPER), CHEAP, TOOLS, GATHERING);
        assignGeneric(HAMMER.getStack(ToolTier.COPPER), CHEAP, TOOLS, MINING);
        assignGeneric(AXE.getStack(ToolTier.SILVER), RARE, TOOLS, GATHERING);
        assignGeneric(HAMMER.getStack(ToolTier.SILVER), RARE, TOOLS, MINING);
        assignGeneric(AXE.getStack(ToolTier.GOLD), RARE, TOOLS, GATHERING);
        assignGeneric(HAMMER.getStack(ToolTier.GOLD), RARE, TOOLS, MINING);
        assignGeneric(AXE.getStack(ToolTier.MYSTRIL), RARE, TOOLS, GATHERING);
        assignGeneric(HAMMER.getStack(ToolTier.MYSTRIL), RARE, TOOLS, MINING);
        assignGeneric(AXE.getStack(ToolTier.CURSED), RARE, TOOLS, GATHERING, SCARY);
        assignGeneric(HAMMER.getStack(ToolTier.CURSED), RARE, TOOLS, MINING, SCARY);
        assignGeneric(AXE.getStack(ToolTier.BLESSED), RARE, TOOLS, GATHERING, PRETTY);
        assignGeneric(HAMMER.getStack(ToolTier.BLESSED), RARE, TOOLS, MINING, PRETTY);
        assignGeneric(AXE.getStack(ToolTier.MYTHIC), RARE, TOOLS, GATHERING);
        assignGeneric(HAMMER.getStack(ToolTier.MYTHIC), RARE, TOOLS, MINING);
    }
}