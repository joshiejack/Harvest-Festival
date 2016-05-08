package joshie.harvest.core.util.generic;

import net.minecraft.util.text.translation.I18n;

public class Text {
    public static String localize(String key) {
        return I18n.translateToLocal(key);
    }

    public static String localizeFully(String key) {
        return I18n.translateToLocal(key.replace("_", "."));
    }
}