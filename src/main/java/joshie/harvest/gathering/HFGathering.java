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
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(BAMBOO), 0.1D, SPRING);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(GRAPES), 0.1D, SUMMER);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(MATSUTAKE), 0.01D, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(TOADSTOOL), 0.05D, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(YELLOW_GRASS), 0.04D, SPRING);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(ORANGE_GRASS), 0.05D, SPRING, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(BLUE_GRASS), 0.05D, SUMMER);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(GREEN_GRASS), 0.05D, SUMMER);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(RED_GRASS), 0.045D, SUMMER, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(PURPLE_GRASS), 0.04D, SUMMER);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(INDIGO_GRASS), 0.05D, SUMMER);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(WHITE_GRASS), 0.025D, AUTUMN);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(MOONDROP), 0.09D, SPRING);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(TOY), 0.035D, SPRING);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(PINKCAT), 0.08D, SUMMER);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(BLUE_MAGICGRASS), 0.07D, AUTUMN);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(RED_MAGICGRASS), 0.07D, AUTUMN);
    }
}
