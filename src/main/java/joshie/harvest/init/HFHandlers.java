package joshie.harvest.init;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.events.AnimalEvents;
import joshie.harvest.core.handlers.events.FMLEvents;
import joshie.harvest.core.handlers.events.GeneralEvents;
import joshie.harvest.core.handlers.events.QuestEvents;
import joshie.harvest.core.handlers.events.ToolEvents;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class HFHandlers {
    public static void init() {
        FMLCommonHandler.instance().bus().register(new FMLEvents());
        MinecraftForge.EVENT_BUS.register(new AnimalEvents());
        MinecraftForge.EVENT_BUS.register(new HFCommands());
        MinecraftForge.EVENT_BUS.register(new GeneralEvents());
        MinecraftForge.EVENT_BUS.register(new QuestEvents());
        MinecraftForge.EVENT_BUS.register(new ToolEvents());
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());
    }
}
