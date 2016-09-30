package joshie.harvest.api.calendar;

import net.minecraft.util.text.translation.I18n;

import java.util.Locale;

public enum Weekday {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    public boolean isWeekend() {
        return this == SATURDAY || this == SUNDAY;
    }

    @SuppressWarnings("deprecation")
    public String getLocalizedName() {
        return I18n.translateToLocal("harvestfestival.calendar." + name().toLowerCase(Locale.ENGLISH));
    }
}