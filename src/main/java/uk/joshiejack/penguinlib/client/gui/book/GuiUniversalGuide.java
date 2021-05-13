package uk.joshiejack.penguinlib.client.gui.book;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.item.custom.ItemCustomGuide;
import uk.joshiejack.penguinlib.client.GuiElements;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiUniversalGuide extends GuiBook {
    private static final Map<ResourceLocation, GuiUniversalGuide> GUIDES = new HashMap<>();
    private final Map<Page, DefaultPage> tabRegistry = Maps.newHashMap();

    private final List<TabBuilder> tabs = Lists.newLinkedList();

    public GuiUniversalGuide() {
        super(GuiElements.BOOK_LEFT, GuiElements.BOOK_RIGHT, 154, 202);
    }

    public static GuiUniversalGuide getGui(ItemCustomGuide guide) {
        if (!GUIDES.containsKey(guide.getRegistryName())) {
            GuiUniversalGuide instance = new GuiUniversalGuide();
            guide.tabs().forEach(tab -> {
                DefaultPage defaultPage = new DefaultPage(tab.name, tab.icon);
                instance.add(defaultPage);
                tab.sub_tabs.stream()
                        .filter(st -> Page.REGISTRY.get(st) != null)
                        .map(Page.REGISTRY::get)
                        .forEach(pg -> {
                            defaultPage.add(pg);
                            instance.tabRegistry.put(pg, defaultPage);
                        });
            });

            GUIDES.put(guide.getRegistryName(), instance);
        }

        return GUIDES.get(guide.getRegistryName());
    }

    public TabBuilder add(Page page) {
        TabBuilder builder = new TabBuilder(page);
        tabs.add(builder);
        return builder;
    }

    @Override
    protected void initTabs(List<GuiButton> buttonList) {
        tabs.sort(Comparator.comparing(TabBuilder::getPriority));
        for (int i = 0; i < tabs.size(); i++) {
            buttonList.add(tabs.get(i).toButton(this, i, buttonList.size()));
        }

        DefaultPage defaultPage = tabRegistry.get(getPage());
        if (defaultPage != null) {
            for (int j = 0; j < defaultPage.tabs.size(); j++) {
                buttonList.add(defaultPage.tabs.get(j).toButton(this, j, buttonList.size()));
            }
        }
    }

    @Override
    public Page getDefaultPage() {
        tabs.sort(Comparator.comparing(TabBuilder::getPriority));
        return tabs.get(0).getPage();
    }
}
