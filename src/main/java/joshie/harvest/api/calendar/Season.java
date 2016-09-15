package joshie.harvest.api.calendar;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum Season {
    SPRING(TextFormatting.GREEN),
    SUMMER(TextFormatting.YELLOW),
    AUTUMN(TextFormatting.GOLD),
    WINTER(TextFormatting.WHITE);

    private final TextFormatting textColor;

    Season(TextFormatting textColor) {
        this.textColor = textColor;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    public String getDisplayName() {
        return textColor + I18n.translateToLocal("harvestfestival.season." + name().toLowerCase(Locale.ENGLISH));
    }
}