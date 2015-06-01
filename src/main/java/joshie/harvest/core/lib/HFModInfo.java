package joshie.harvest.core.lib;

import net.minecraft.util.ResourceLocation;

public class HFModInfo {
    public static final String MODID = "HarvestFestival";
    public static final String MODNAME = "Harvest Festival";
    public static final String MODPATH = "hf";
    public static final String JAVAPATH = "joshie.harvest.";
    public static final String CAPNAME = "HF";
    public static final String BLOCKSNAME = "HFBlocks";
    public static final String ASMPATH = "joshie/harvest/";
    public static final String COMMANDNAME = "hf";
    
    public static final String BUILDINGPATH = "hf:buildings/";
    public static final String CROPPATH = "hf:crops/";
    public static final String MEALPATH = "hf:meals/";
    public static final String SEEDPATH = "hf:seeds/";
    public static final String TREATPATH = "hf:treats/";
    public static final String VERSION = "@VERSION@";
    
    public static final ResourceLocation elements = new ResourceLocation(MODPATH, "textures/gui/gui_elements.png");
    public static ResourceLocation stars = new ResourceLocation(MODPATH, "textures/gui/gui_stars.png");
    public static final int FARMLAND_MINE_HOED_META = 15;
}
