package uk.joshiejack.penguinlib.client.gui.book;

import com.google.common.collect.Lists;
import joptsimple.internal.Strings;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.data.custom.CustomIcon;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import java.util.Comparator;
import java.util.List;

public class DefaultPage extends Page {
    public final List<TabBuilder> tabs = Lists.newLinkedList();
    public Page last;

    public DefaultPage(String name, CustomIcon icon) {
        super(name);
        this.icon = !Strings.isNullOrEmpty(icon.item) ? new Page.Icon(StackHelper.getStackFromString(icon.item), icon.x, icon.y) :
                new Page.Icon(new ResourceLocation(icon.gui), icon.x, icon.y);
    }

    public TabBuilder add(Page page) {
        TabBuilder builder = new TabBuilder(page).setRight().setDefault(this);
        if (!tabs.contains(builder)) {
            tabs.add(builder);
        }

        return builder;
    }

    public Page getActualPage() {
        if (tabs.size() == 0) {
            return null;

        }
        if (last == null) {
            tabs.sort(Comparator.comparing(TabBuilder::getPriority));
            last = tabs.get(0).getPage();
        }

        return last;
    }
}
