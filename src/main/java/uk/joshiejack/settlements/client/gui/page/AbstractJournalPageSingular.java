package uk.joshiejack.settlements.client.gui.page;

import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.item.ItemStack;

public abstract class AbstractJournalPageSingular extends Page {
    public AbstractJournalPageSingular(String name, ItemStack icon) {
        super("settlements.journal." + name);
        this.icon = new Icon(GuiElements.ICONS, 0, 0);//TODO: Proper icon
    }
}
