package joshie.harvest.core.lib;

import net.minecraft.util.ResourceLocation;

public class HFModInfo {
    public static final String MODID = "harvestfestival";
    public static final String MODNAME = "Harvest Festival";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "required-after:forge@[13.20,);after:immersiveengineering";
    public static final String GUI_FACTORY = "joshie.harvest.core.util.HFGuiFactory";
    public static final String JAVAPATH = "joshie.harvest.";
    public static final String CAPNAME = "HF";
    public static final String COMMANDNAME = "hf";
    public static final String GIFTPATH = "joshie.harvest.npcs.gift.Gifts";
    public static final String SCHEDULEPATH = "joshie.harvest.npcs.schedule.Schedule";
    public static final String CROPSTATES = "joshie.harvest.crops.handlers.state.StateHandler";
    public static final String DROPHANDLERS = "joshie.harvest.crops.handlers.drop.DropHandler";
    public static final String GROWTHHANDLERS = "joshie.harvest.crops.handlers.growth.GrowthHandler";
    public static final String RULES = "joshie.harvest.crops.handlers.rules.SpecialRules";

    //Textures
    public static final ResourceLocation ELEMENTS = new ResourceLocation(MODID, "textures/gui/gui_elements.png");
    public static final ResourceLocation TOOLELEMENTS = new ResourceLocation(MODID, "textures/gui/gui_toolelements.png");
    public static final ResourceLocation ICONS = new ResourceLocation(MODID, "textures/gui/icons.png");
}