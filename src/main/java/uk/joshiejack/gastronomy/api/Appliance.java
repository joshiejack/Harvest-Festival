package uk.joshiejack.gastronomy.api;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum Appliance implements IStringSerializable {
    COUNTER, FRYING_PAN, MIXER, OVEN, POT;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
