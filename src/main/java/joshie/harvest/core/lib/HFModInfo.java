package joshie.harvest.core.lib;

import net.minecraft.util.ResourceLocation;

public class HFModInfo {
    public static final String MODID = "harvestfestival";
    public static final String MODNAME = "Harvest Festival";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "required-after:Forge@[12.18.1.2045,)";
    public static final String GUI_FACTORY = "joshie.harvest.core.util.HFGuiFactory";
    public static final String JAVAPATH = "joshie.harvest.";
    public static final String CAPNAME = "HF";
    public static final String COMMANDNAME = "hf";
    public static final String GIFTPATH = "joshie.harvest.npc.gift.Gifts";
    public static final String SCHEDULEPATH = "joshie.harvest.npc.schedule.Schedule";

    
    public static final ResourceLocation ELEMENTS = new ResourceLocation(MODID, "textures/gui/gui_elements.png");
    public static final ResourceLocation STARS = new ResourceLocation(MODID, "textures/gui/gui_stars.png");
}