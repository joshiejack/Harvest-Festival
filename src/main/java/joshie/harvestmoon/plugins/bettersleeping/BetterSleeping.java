package joshie.harvestmoon.plugins.bettersleeping;

import joshie.harvestmoon.core.handlers.events.FMLEvents;
import joshie.harvestmoon.plugins.HMPlugins.Plugin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cz.ondraster.bettersleeping.api.WorldSleepEvent;

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
        FMLEvents.newDay(false);
    }
}
