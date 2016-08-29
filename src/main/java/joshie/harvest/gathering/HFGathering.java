package joshie.harvest.gathering;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.gathering.block.BlockRock;
import joshie.harvest.gathering.block.BlockWood;
import joshie.harvest.core.HFCore;

import static joshie.harvest.gathering.block.BlockRock.Rock.*;
import static joshie.harvest.gathering.block.BlockWood.Wood.*;

@HFLoader
public class HFGathering {
    public static final BlockRock ROCK = new BlockRock().register("rock");
    public static final BlockWood WOOD = new BlockWood().register("wood");

    public static void preInit() {}
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
        HFApi.gathering.registerGathering(HFCore.FLOWERS.getStateFromEnum(FlowerType.WEED), 10D);
    }
}
