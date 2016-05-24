package joshie.harvest.npc.town.gathering;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.BlockFlower.FlowerType;
import joshie.harvest.blocks.BlockGathering.GatheringType;
import joshie.harvest.blocks.HFBlocks;

public class HFGathering {
    public static void init() {
        HFApi.gathering.registerGathering(HFBlocks.FLOWERS.getStateFromEnum(FlowerType.WEED), 10D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.BRANCH_SMALL), 8D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.BRANCH_MEDIUM), 5D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.BRANCH_LARGE), 3D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.ROCK_SMALL), 8D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.ROCK_MEDIUM), 5D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.ROCK_LARGE), 3D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.STUMP_SMALL), 3D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.STUMP_MEDIUM), 2D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.STUMP_LARGE), 1D);
    }
}
