package joshie.harvest.npc.gift.init;

import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.gathering.block.BlockRock.Rock;
import joshie.harvest.gathering.block.BlockWood.Wood;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.core.HFCore.FLOWERS;
import static joshie.harvest.gathering.HFGathering.ROCK;
import static joshie.harvest.gathering.HFGathering.WOOD;

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
    }
}