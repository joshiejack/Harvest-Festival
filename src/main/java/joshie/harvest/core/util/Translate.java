package joshie.harvest.core.util;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.util.text.translation.I18n;

public class Translate {
    public static String translate(String s) {
        return I18n.translateToLocal(HFModInfo.MODID + "." + s);
    }

    public static String translate(String mod, String s) {
        return I18n.translateToLocal(mod + "." + s);
    }
}