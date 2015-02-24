package joshie.harvestmoon.core.util;

import joshie.harvestmoon.core.lib.HMModInfo;
import net.minecraft.util.StatCollector;

public class Translate {
    public static String translate(String s) {
        return StatCollector.translateToLocal(HMModInfo.MODPATH + "." + s);
    }
}
