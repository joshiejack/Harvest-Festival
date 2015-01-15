package joshie.harvestmoon.util;

import net.minecraft.util.StatCollector;

public class Translate {
    public static String translate(String s) {
        return StatCollector.translateToLocal("hm." + s);
    }
}
