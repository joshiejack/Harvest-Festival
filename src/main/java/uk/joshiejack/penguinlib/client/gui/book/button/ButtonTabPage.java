package uk.joshiejack.penguinlib.client.gui.book.button;

import uk.joshiejack.penguinlib.client.gui.book.DefaultPage;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import net.minecraft.util.ResourceLocation;

public class ButtonTabPage extends AbstractButtonTab {
    private DefaultPage default_;
    private Page.Icon icon;

    public ButtonTabPage(GuiBook book, Page page, int buttonId, int x, int y, boolean right) {
        super(book, page, buttonId, x + (right ? 334 : 0), y, right ? 0: 26, right ? 0 : 10);
    }

    public ButtonTabPage setDefault(DefaultPage default_) {
        this.default_ = default_;
        return this;
    }

    @Override
    public Page.Icon getIcon() {
        return icon != null ? icon : super.getIcon();
    }

    public ButtonTabPage setIcon(ResourceLocation texture, int x, int z) {
        if (texture != null) {
            this.icon = new Page.Icon(texture, x, z);
        }

        return this;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
        if (default_ != null) {
            default_.last = page; //Update the last page
        }
    }
}
