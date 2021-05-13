package uk.joshiejack.settlements.world.town.people;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import java.util.Locale;

public enum Ordinance {
    INTERNAL_PROTECTION(true), //Protect buildings from being destroyed by your own members
    EXTERNAL_PROTECTION(false), //Protect buildings from being destroyed by non members
    NO_KILL(false),  //Disable PVP in town limits
    BAN_CONSTRUCTION(false), //Non members cannot build on your land
    BAN_COMMUNICATION(false), //Non members cannot talk with your residents
    BAN_INTERACTION(false), //Non members cannot talk with your blocks
    BAN_ITEM_USAGE(false); //Non members cannot use any items within your town except for food

    private final boolean enabled;

    Ordinance(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return StringHelper.localize(Settlements.MODID + ".ordinance." + name().toLowerCase(Locale.ENGLISH) + ".name");
    }

    public String getDescription() {
        return StringHelper.localize(Settlements.MODID + ".ordinance." + name().toLowerCase(Locale.ENGLISH) + ".description");
    }

    public String getTooltip() {
        return StringHelper.localize(Settlements.MODID + ".ordinance." + name().toLowerCase(Locale.ENGLISH) + ".tooltip");
    }

    public boolean isEnabledByDefault() {
        return enabled;
    }
}
