package joshie.harvest.knowledge.gui.stats.notes.page;

import joshie.harvest.api.knowledge.Category;
import joshie.harvest.core.base.gui.BookPage;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageOther extends PageNotes {
    public static final BookPage INSTANCE = new PageOther();

    PageOther() {
        super(Category.OTHER, new ItemStack(Items.COMPASS));
    }
}
