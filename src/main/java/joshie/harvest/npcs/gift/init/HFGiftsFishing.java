package joshie.harvest.npcs.gift.init;

import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

@HFLoader(priority = 0)
public class HFGiftsFishing extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(HFFishing.JUNK.getStackFromEnum(Junk.BAIT), JUNK);
        assignGeneric(HFFishing.JUNK.getStackFromEnum(Junk.BONES), JUNK);
        assignGeneric(HFFishing.JUNK.getStackFromEnum(Junk.BOOT), JUNK);
        assignGeneric(HFFishing.JUNK.getStackFromEnum(Junk.CAN), JUNK);
        assignGeneric(HFFishing.JUNK.getStackFromEnum(Junk.FOSSIL), KNOWLEDGE);
        assignGeneric(HFFishing.JUNK.getStackFromEnum(Junk.TREASURE), GEM);
    }
}