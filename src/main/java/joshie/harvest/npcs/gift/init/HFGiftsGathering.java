package joshie.harvest.npcs.gift.init;

import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.gathering.block.BlockNature.NaturalBlock;
import joshie.harvest.gathering.block.BlockRock.Rock;
import joshie.harvest.gathering.block.BlockWood.Wood;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.core.HFCore.FLOWERS;
import static joshie.harvest.gathering.HFGathering.*;

@HFLoader(priority = 0)
public class HFGiftsGathering extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(NATURE.getStackFromEnum(NaturalBlock.BAMBOO), VEGETABLE);
        assignGeneric(NATURE.getStackFromEnum(NaturalBlock.MATSUTAKE), MUSHROOM);
        assignGeneric(NATURE.getStackFromEnum(NaturalBlock.CHAMOMILE), HERB);
        assignGeneric(NATURE.getStackFromEnum(NaturalBlock.MINT), HERB);
        assignGeneric(NATURE.getStackFromEnum((NaturalBlock.LAVENDER)), HERB);
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.WEED), JUNK);
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.GODDESS), FLOWER);
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.PINKCAT), FLOWER);
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.TOY), FLOWER);
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.BLUE_MAGICGRASS), FLOWER);
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.MOONDROP), FLOWER);
        assignGeneric(FLOWERS.getStackFromEnum(FlowerType.RED_MAGICGRASS), FLOWER);
        assignGeneric(WOOD.getStackFromEnum(Wood.BRANCH_SMALL), JUNK);
        assignGeneric(WOOD.getStackFromEnum(Wood.BRANCH_MEDIUM), JUNK);
        assignGeneric(WOOD.getStackFromEnum(Wood.BRANCH_LARGE), JUNK);
        assignGeneric(WOOD.getStackFromEnum(Wood.STUMP_SMALL), JUNK);
        assignGeneric(WOOD.getStackFromEnum(Wood.STUMP_MEDIUM), JUNK);
        assignGeneric(WOOD.getStackFromEnum(Wood.STUMP_LARGE), JUNK);
        assignGeneric(ROCK.getStackFromEnum(Rock.STONE_SMALL), JUNK);
        assignGeneric(ROCK.getStackFromEnum(Rock.STONE_MEDIUM), JUNK);
        assignGeneric(ROCK.getStackFromEnum(Rock.STONE_LARGE), JUNK);
        assignGeneric(ROCK.getStackFromEnum(Rock.BOULDER_SMALL), JUNK);
        assignGeneric(ROCK.getStackFromEnum(Rock.BOULDER_MEDIUM), JUNK);
        assignGeneric(ROCK.getStackFromEnum(Rock.BOULDER_LARGE), JUNK);
    }
}