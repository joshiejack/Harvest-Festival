package uk.joshiejack.settlements.world.town.people;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import java.util.Locale;

public enum Citizenship {
    CLOSED, OPEN, APPLICATION;

    public String getName() {
        return StringHelper.localize(Settlements.MODID + ".citizenship." + name().toLowerCase(Locale.ENGLISH) + ".name");
    }

    public String getType() {
        return StringHelper.localize(Settlements.MODID + ".citizenship.name");
    }

    public String getDescription() {
        return StringHelper.localize(Settlements.MODID + ".citizenship." + name().toLowerCase(Locale.ENGLISH) + ".description");
    }

    public String getTooltip() {
        return StringHelper.localize(Settlements.MODID + ".citizenship." + name().toLowerCase(Locale.ENGLISH) + ".tooltip");
    }

    public Citizenship next() {
        return this == Citizenship.OPEN ? Citizenship.APPLICATION : this == Citizenship.APPLICATION ? Citizenship.CLOSED : Citizenship.OPEN;
    }
}
