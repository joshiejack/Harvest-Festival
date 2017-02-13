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

    //256 Total
    @SuppressWarnings("deprecation")
    public static void init() {
        //Seasons add up to 64
        //Spring
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(WEED), 18D, SPRING);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(MOONDROP), 5D, SPRING);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(TOY), 3D, SPRING);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(BAMBOO), 11D, SPRING);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(CHAMOMILE), 13D, SPRING);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(MINT), 14D, SPRING);

        //Summer
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(WEED), 20D, SUMMER);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(PINKCAT), 5D, SUMMER);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(CHAMOMILE), 19D, SUMMER);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(MINT), 20D, SUMMER);

        //Autumn
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(WEED), 20D, AUTUMN);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(BLUE_MAGICGRASS), 4D, AUTUMN);
        HFApi.gathering.registerGathering(FLOWERS.getStateFromEnum(RED_MAGICGRASS), 2D, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(MATSUTAKE), 1D, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(LAVENDAR), 10D, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(CHAMOMILE), 12D, AUTUMN);
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(MINT), 15D, AUTUMN);

        //Winter
        HFApi.gathering.registerGathering(NATURE.getStateFromEnum(LAVENDAR), 64D, WINTER);

        //All Seasons, Adds to 192
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(BRANCH_SMALL), 27D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(BRANCH_MEDIUM), 18D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(BRANCH_LARGE), 10D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(STONE_SMALL), 28D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(STONE_MEDIUM), 13D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(STONE_LARGE), 8D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(STUMP_SMALL), 25D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(STUMP_MEDIUM), 10D);
        HFApi.gathering.registerGathering(WOOD.getStateFromEnum(STUMP_LARGE), 8D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(BOULDER_SMALL), 28D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(BOULDER_MEDIUM), 10D);
        HFApi.gathering.registerGathering(ROCK.getStateFromEnum(BOULDER_LARGE), 7D);
    }
}
