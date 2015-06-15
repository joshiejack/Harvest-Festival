package joshie.harvest.init;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.AnimalEvents;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.events.FMLEvents;
import joshie.harvest.core.handlers.events.GeneralEvents;
import joshie.harvest.quests.QuestEvents;
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
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());
    }
}
