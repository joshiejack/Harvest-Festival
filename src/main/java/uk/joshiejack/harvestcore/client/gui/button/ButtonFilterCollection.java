package uk.joshiejack.harvestcore.client.gui.button;

import uk.joshiejack.economy.shipping.ShippingRegistry;
import uk.joshiejack.harvestcore.client.gui.page.PageCollection;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonTextField;
import net.minecraft.item.ItemStack;

import java.util.Locale;

public class ButtonFilterCollection extends ButtonTextField {
    private final PageCollection collection;

    public ButtonFilterCollection(GuiBook gui, PageCollection collection, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, 14);
        this.collection = collection;
    }

    @Override
    public String getText() {
        return collection.getSearch();
    }

    @Override
    public void setText(String textA) {
        collection.setFilter(textA, (stack) -> {
            long value = ShippingRegistry.INSTANCE.getValue(stack);
            String text = textA.toLowerCase(Locale.ENGLISH);
            if (text.startsWith("=")) {
                try {
                    return value == Long.parseLong(text.substring(1).trim());
                } catch (Exception ignored) {}
            } else if (text.startsWith("<=")) {
                try {
                    return value <= Long.parseLong(text.substring(2).trim());
                } catch (Exception ignored) {}
            } else if (text.startsWith("<")) {
                try {
                    return value < Long.parseLong(text.substring(1).trim());
                } catch (Exception ignored) {}
            } else if (text.startsWith(">=")) {
                try {
                    return value >= Long.parseLong(text.substring(2).trim());
                } catch (Exception ignored) {}
            } else if (text.startsWith(">")) {
                try {
                    return value > Long.parseLong(text.substring(1).trim());
                } catch (Exception ignored) {}
            } else if (text.startsWith("@")) {
                String modid = stack.getItem().getRegistryName().getNamespace().toLowerCase();
                return modid.contains(text.substring(1));
            }

            return stack.getDisplayName().toLowerCase().contains(text);
        });
    }

    public interface Filter {
        boolean contains(ItemStack stack);
    }
}
