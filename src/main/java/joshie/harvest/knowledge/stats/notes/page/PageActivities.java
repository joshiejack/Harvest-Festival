package joshie.harvest.knowledge.stats.notes.page;

import joshie.harvest.api.knowledge.Category;
import joshie.harvest.core.base.gui.BookPage;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageActivities extends PageNotes {
    public static final BookPage INSTANCE = new PageActivities();

    PageActivities() {
        super(Category.ACTIVITIES, new ItemStack(Items.FISHING_ROD));
    }
}
