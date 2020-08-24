package joshie.harvest.knowledge;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.knowledge.block.BlockCalendar;
import joshie.harvest.knowledge.item.ItemBook;

@HFLoader
public class HFKnowledge {
    public static final BlockCalendar CALENDAR = new BlockCalendar().register("calendar");
    public static final ItemBook BOOK = new ItemBook().register("book");

    public static void preInit() {
        HFApi.npc.getGifts().addToBlacklist(BOOK);
    }
}