package joshie.harvest.npc.gift.init;


import joshie.harvest.core.util.HFLoader;

@HFLoader(priority = 0)
public class HFGifts {
    public static void preInit() {
        HFGiftVanillaItems.init();
        HFGiftVanillaBlocks.init();
    }
}