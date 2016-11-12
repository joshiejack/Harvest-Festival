package joshie.harvest.core.base.gui;

import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BookPage<G extends GuiBaseBook> {
    private final ItemStack icon;
    private final String name;
    private final String category;
    protected G gui;

    public BookPage (String category, String name, ItemStack stack) {
        this.category = "harvestfestival.stats." + category;
        this.name = "harvestfestival.stats." + name;
        this.icon = stack;
    }

    public String getCategory() {
        return TextHelper.localize(category);
    }

    public String getDisplayName() {
        return TextHelper.localize(name);
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void initGui(G gui, List<GuiButton> buttonList) {
        this.gui = gui;
    }
}
