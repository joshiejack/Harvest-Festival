package harvestmoon.lib;

import net.minecraft.util.ResourceLocation;

public class ModInfo {
    public static final String MODID = "HarvestMoon";
    public static final String MODNAME = "Harvest Moon";
    public static final String MODPATH = "hm";
    
    public static final String CROPPATH = "hm:crops/";
    public static final String MEALPATH = "hm:meals/";
    public static final String SEEDPATH = "hm:seeds/";
    
    public static final ResourceLocation elements = new ResourceLocation(MODPATH, "textures/gui/gui_elements.png");
    public static ResourceLocation stars = new ResourceLocation(MODPATH, "textures/gui/gui_stars.png");
}
