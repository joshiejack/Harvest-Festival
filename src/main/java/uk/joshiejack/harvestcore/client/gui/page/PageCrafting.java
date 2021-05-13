package uk.joshiejack.harvestcore.client.gui.page;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import uk.joshiejack.harvestcore.client.gui.button.ButtonCraft;
import uk.joshiejack.harvestcore.client.gui.button.ButtonFilterCrafting;
import uk.joshiejack.harvestcore.client.gui.button.PageSearchable;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;

import java.util.List;
import java.util.stream.Collectors;

import static uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple.PageSide.LEFT;

public class PageCrafting extends PageSearchable.MultipleButton<Blueprint> {
    private final String category;
    private ButtonFilterCrafting.Filter filter = b -> true;

    public PageCrafting(String category, ItemStack icon) {
        super("harvestcore.guide.crafting." + category, 49);
        this.category = category;
        this.icon = new Page.Icon(icon, 0, 0);
        this.container = 0;
    }

    public void setFilter(String text, ButtonFilterCrafting.Filter filter) {
        this.search = text;
        this.filter = filter;
    }

    @Override
    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(buttonList, labelList);
        buttonList.add(new ButtonFilterCrafting(gui, this, buttonList.size(), 177, 202));
    }

    @Override
    protected ButtonBook createButton(GuiBook gui, Blueprint entry, PageSide side, int id, int position) {
        int x = position % 7;
        int y = position / 7;
        return new ButtonCraft(gui, entry, id, (side == LEFT ? 21 : 163) + (x * 18), 24 + (y * 18));
    }

    @Override
    public List<Blueprint> getList() {
        return Blueprint.REGISTRY.values().stream()
                .filter(b -> filter.contains(b) && (b.category().equals(category) && b.isUnlocked(gui.mc.player))).collect(Collectors.toList());
    }
}
