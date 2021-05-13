package uk.joshiejack.harvestcore.client.gui.page;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.economy.shipping.ShippingRegistry;
import uk.joshiejack.harvestcore.client.gui.button.ButtonFilterCollection;
import uk.joshiejack.harvestcore.client.gui.button.PageSearchable;
import uk.joshiejack.harvestcore.client.gui.label.LabelShippingCollection;
import uk.joshiejack.harvestcore.database.Collections;
import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;

import java.util.List;

public class PageCollection extends PageSearchable.Multiple<Pair<NonNullList<ItemStack>, Long>> {
    private final Collections.Collection collection;
    private ButtonFilterCollection.Filter filter = (s) -> true;

    public PageCollection(Collections.Collection collection) {
        super(collection.getName(), 112);
        this.icon = collection.getIcon();
        this.collection = collection;
    }

    public void setFilter(String text, ButtonFilterCollection.Filter filter) {
        this.search = text;
        this.filter = filter;
    }

    @Override
    public List<Pair<NonNullList<ItemStack>, Long>> getList() {
        List<Pair<NonNullList<ItemStack>, Long>> entries = Lists.newArrayList();
        if (collection.isShippingCollection()) collection.getList().stream().filter(p -> p.stream().anyMatch(s -> filter.contains(s))).forEach(list -> entries.add(Pair.of(list, ShippingRegistry.INSTANCE.getValue(list.get(0)))));
        else collection.getList().stream().filter(p -> p.stream().anyMatch(s -> filter.contains(s))).forEach(list -> entries.add(Pair.of(list, (long) collection.get("getValue", gui.mc.player, list.get(0)))));
        return entries;
    }


    @Override
    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
        labelList.add(createLabel(TextFormatting.UNDERLINE + getDisplayName(), labelList.size(), gui.getGuiLeft() + 60, gui.getGuiTop() + 8, 60, 20));
        buttonList.add(new ButtonFilterCollection(gui, this, buttonList.size(), 177, 202));
        super.initGui(buttonList, labelList);
    }

    @Override
    protected void addToList(List<GuiButton> buttonList, List<GuiLabel> labelList, GuiBook gui, Pair<NonNullList<ItemStack>, Long> pair, PageSide side, int position) {
        labelList.add(new LabelShippingCollection(gui, (side == PageSide.LEFT ? 21 : 163) + (position % 7) * 18, 24 + (position / 7) * 18, new CyclingStack(pair.getKey()), pair.getValue(), collection));
    }
}
