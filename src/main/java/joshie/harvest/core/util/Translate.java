package joshie.harvest.core.util;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.util.StatCollector;

public class Translate {
    public static String translate(String s) {
        return StatCollector.translateToLocal(HFModInfo.MODPATH + "." + s);
    }
}
