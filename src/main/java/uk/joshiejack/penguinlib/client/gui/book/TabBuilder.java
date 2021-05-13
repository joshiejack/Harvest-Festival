package uk.joshiejack.penguinlib.client.gui.book;

import uk.joshiejack.penguinlib.client.gui.book.button.ButtonTabPage;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import java.util.Objects;

public class TabBuilder {
    private final Page page;
    private DefaultPage default_;
    private ResourceLocation texture;
    private int x, z;
    private boolean right;

    public TabBuilder(Page page) {
        this.page = page;
    }

    public EventPriority getPriority() { return EventPriority.NORMAL; }

    public GuiButton toButton(GuiUniversalGuide guide, int position, int id) {
        return new ButtonTabPage(guide, page, id, -26, 18 + (position * 34), right).setIcon(texture, x, z).setDefault(default_);
    }

    public TabBuilder setDefault(DefaultPage default_) {
        this.default_ = default_;
        return this;
    }

    public Page getPage() {
        return this.page;
    }

    public void setIcon(ResourceLocation texture, int x, int z) {
        this.texture = texture;
        this.x = x;
        this.z = z;
    }

    public TabBuilder setRight() {
        this.right = true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabBuilder builder = (TabBuilder) o;
        return Objects.equals(page, builder.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page);
    }
}
