package joshie.harvest.npc.gift.init;

import joshie.harvest.core.util.HFLoader;
import joshie.harvest.mining.block.BlockOre.Ore;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.mining.HFMining.*;

@HFLoader(priority = 0)
public class HFGiftsMining extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(STONE, CHEAP, MINING);
        assignGeneric(DIRT_DECORATIVE, CHEAP, MINING);
        assignGeneric(ORE.getStackFromEnum(Ore.ROCK), CHEAP, MINING);
        assignGeneric(ORE.getStackFromEnum(Ore.SILVER), RARE, MINING);
        assignGeneric(ORE.getStackFromEnum(Ore.MYSTRIL), RARE, MINING, PRETTY);
        assignGeneric(ORE.getStackFromEnum(Ore.GOLD), RARE, MINING, PRETTY);
        assignGeneric(ORE.getStackFromEnum(Ore.COPPER), CHEAP, MINING);
    }
}