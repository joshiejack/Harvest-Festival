package joshie.harvestmoon.init;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.core.handlers.GuiHandler;
import joshie.harvestmoon.core.handlers.events.AnimalEvents;
import joshie.harvestmoon.core.handlers.events.CropEvents;
import joshie.harvestmoon.core.handlers.events.FMLEvents;
import joshie.harvestmoon.core.handlers.events.GeneralEvents;
import joshie.harvestmoon.core.handlers.events.QuestEvents;
import joshie.harvestmoon.core.handlers.events.ToolEvents;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class HMHandlers {
    public static void init() {
        FMLCommonHandler.instance().bus().register(new FMLEvents());
        MinecraftForge.EVENT_BUS.register(new AnimalEvents());
        MinecraftForge.EVENT_BUS.register(new CropEvents());
        MinecraftForge.EVENT_BUS.register(new HMCommands());
        MinecraftForge.EVENT_BUS.register(new GeneralEvents());
        MinecraftForge.EVENT_BUS.register(new QuestEvents());
        MinecraftForge.EVENT_BUS.register(new ToolEvents());
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestMoon.instance, new GuiHandler());
    }
}
