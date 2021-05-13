package uk.joshiejack.harvestcore.client.gui.button;

import uk.joshiejack.harvestcore.client.gui.page.PageCrafting;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonTextField;
import net.minecraft.item.ItemStack;

public class ButtonFilterCrafting extends ButtonTextField {
    private final PageCrafting crafting;

    public ButtonFilterCrafting(GuiBook gui, PageCrafting crafting, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, 14);
        this.crafting = crafting;
    }

    @Override
    public String getText() {
        return crafting.getSearch();
    }

    @Override
    public void setText(String text) {
        crafting.setFilter(text, (blueprint) -> {
            ItemStack result = blueprint.getResult();
            if (text.startsWith("@")) {
                String modid = result.getItem().getRegistryName().getNamespace();
                return modid.contains(text.substring(1));
            }

            return result.getDisplayName().toLowerCase().contains(text);
        });
    }

    public interface Filter {
        boolean contains(Blueprint blueprint);
    }
}
