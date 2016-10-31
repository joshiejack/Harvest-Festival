package joshie.harvest.gathering;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.gathering.block.BlockNature;
import joshie.harvest.gathering.block.BlockRock;
import joshie.harvest.gathering.block.BlockWood;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.HFCore.FLOWERS;
import static joshie.harvest.core.block.BlockFlower.FlowerType.*;
import static joshie.harvest.gathering.block.BlockNature.NaturalBlock.*;
import static joshie.harvest.gathering.block.BlockRock.Rock.*;
import static joshie.harvest.gathering.block.BlockWood.Wood.*;

@HFLoader
public class HFGathering {
    public static final BlockNature NATURE = new BlockNature().register("nature");
    public static final BlockRock ROCK = new BlockRock().register("rock");
    public static final BlockWood WOOD = new BlockWood().register("wood");

    public static void preInit() {
        //To init the blocks
    }

    public static void init() {
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(BRANCH_SMALL), 8D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(BRANCH_MEDIUM), 5D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(BRANCH_LARGE), 3D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(STONE_SMALL), 8D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(STONE_MEDIUM), 5D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(STONE_LARGE), 3D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(STUMP_SMALL), 3D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(STUMP_MEDIUM), 2D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(STUMP_LARGE), 1D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(BOULDER_SMALL), 3D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(BOULDER_MEDIUM), 2D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(BOULDER_LARGE), 1D);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(WEED), 10D, SPRING, SUMMER, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(MINT), 1D, SUMMER, SUMMER, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(CHAMOMILE), 1D, SPRING, SUMMER, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(LAVENDAR), 1D, AUTUMN, WINTER);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(BAMBOO), 1D, SPRING);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(MATSUTAKE), 0.1D, AUTUMN);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(MOONDROP), 0.9D, SPRING);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(TOY), 0.35D, SPRING);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(PINKCAT), 0.8D, SUMMER);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(BLUE_MAGICGRASS), 0.7D, AUTUMN);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(RED_MAGICGRASS), 0.7D, AUTUMN);
    }
}
