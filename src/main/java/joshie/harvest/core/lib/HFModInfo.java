package joshie.harvest.core.lib;

import net.minecraft.util.ResourceLocation;

public class HFModInfo {
    public static final String MODID = "harvestfestival";
    public static final String MODNAME = "Harvest Festival";
    public static final String JAVAPATH = "joshie.harvest.";
    public static final String CAPNAME = "HF";
    public static final String BLOCKSNAME = "HFBlocks";
    public static final String COMMANDNAME = "hf";
    
    public static final String BUILDINGPATH = "harvestfestival:buildings/";
    public static final String CROPPATH = "harvestfestival:crops/";
    public static final String MEALPATH = "harvestfestival:meals/";
    public static final String SEEDPATH = "harvestfestival:seeds/";
    public static final String TREATPATH = "harvestfestival:treats/";
    public static final String TOOLSPATH = "harvestfestival:tools/";
    public static final String CTMPATH = "harvestfestival:ctm/";
    public static final String VERSION = "@VERSION@";

    
    public static final ResourceLocation elements = new ResourceLocation(MODID, "textures/gui/gui_elements.png");
    public static ResourceLocation stars = new ResourceLocation(MODID, "textures/gui/gui_stars.png");
    public static final int FARMLAND_MINE_HOED_META = 15;
}