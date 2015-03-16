package joshie.harvestmoon.core.lib;

import net.minecraft.util.ResourceLocation;

public class HMModInfo {
    public static final String MODID = "HarvestMoon";
    public static final String MODNAME = "Harvest Moon";
    public static final String MODPATH = "hm";
    public static final String JAVAPATH = "joshie.harvestmoon.";
    public static final String CAPNAME = "HM";
    public static final String BLOCKSNAME = "HMBlocks";
    public static final String ASMPATH = "joshie/harvestmoon/";
    public static final String COMMANDNAME = "hm";
    
    public static final String CROPPATH = "hm:crops/";
    public static final String MEALPATH = "hm:meals/";
    public static final String SEEDPATH = "hm:seeds/";
    public static final String TREATPATH = "hm:treats/";
    public static final String VERSION = "@VERSION@";
    
    public static final ResourceLocation elements = new ResourceLocation(MODPATH, "textures/gui/gui_elements.png");
    public static ResourceLocation stars = new ResourceLocation(MODPATH, "textures/gui/gui_stars.png");
    public static final int FARMLAND_MINE_HOED_META = 15;
}
