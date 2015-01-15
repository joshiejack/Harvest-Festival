package joshie.lib.util;

import net.minecraft.util.StatCollector;

public class Text {
    private final static String prfx = "\u00a7";
    public static final String BLACK = prfx + "0";
    public static final String DARK_BLUE = prfx + "1";
    public static final String DARK_GREEN = prfx + "2";
    public static final String DARK_AQUA = prfx + "3";
    public static final String DARK_RED = prfx + "4";
    public static final String PURPLE = prfx + "5";
    public static final String ORANGE = prfx + "6";
    public static final String GREY = prfx + "7";
    public static final String DARK_GREY = prfx + "8";
    public static final String INDIGO = prfx + "9";
    public static final String BRIGHT_GREEN = prfx + "a";
    public static final String AQUA = prfx + "b";
    public static final String RED = prfx + "c";
    public static final String PINK = prfx + "d";
    public static final String YELLOW = prfx + "e";
    public static final String WHITE = prfx + "f";
    public static final String BOLD = prfx + "l";
    public static final String UNDERLINE = prfx + "n";
    public static final String ITALIC = prfx + "o";
    public static final String END = prfx + "r";
    public static final String OBFUSCATED = prfx + "k";
    public static final String STRIKETHROUGH = prfx + "m";
    public static final String DEGREES = "\u00B0" + "C";

    public static String localize(String key) {
        return StatCollector.translateToLocal(key);
    }

    public static String removeDecimals(String name) {
        String theName;
        String[] aName = name.split("\\.");
        if (aName.length == 2) {
            theName = aName[0] + aName[1].substring(0, 1).toUpperCase() + aName[1].substring(1);
        } else {
            theName = name;
        }
        
        return theName;
    }
}
