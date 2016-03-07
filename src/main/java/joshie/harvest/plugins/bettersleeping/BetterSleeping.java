package joshie.harvest.plugins.bettersleeping;

import joshie.harvest.core.handlers.EventsHandler;
import joshie.harvest.plugins.HFPlugins.Plugin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BetterSleeping extends Plugin {
    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void loadConfig(Configuration config) {}

    @Override
    public void init() {}

    @Override
    public void postInit() {}

    @SubscribeEvent
    public void onWorldSleep(WorldSleepEvent.Post event) {
        EventsHandler.newDay(event.world, false);
    }
}
