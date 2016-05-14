package joshie.harvest.npc.town.gathering;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.BlockFlower.FlowerType;
import joshie.harvest.blocks.BlockGathering.GatheringType;
import joshie.harvest.blocks.HFBlocks;

public class HFGathering {
    public static void init() {
        HFApi.gathering.registerGathering(HFBlocks.FLOWERS.getStateFromEnum(FlowerType.WEED), 10D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.BRANCH), 8D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.ROCK), 8D);
        HFApi.gathering.registerGathering(HFBlocks.GATHERING.getStateFromEnum(GatheringType.STUMP), 3D);
    }
}
