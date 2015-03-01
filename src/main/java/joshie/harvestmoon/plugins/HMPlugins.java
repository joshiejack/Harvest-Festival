package joshie.harvestmoon.plugins;

import java.util.ArrayList;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.plugins.bettersleeping.BetterSleeping;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.Loader;

public class HMPlugins {
    public static ArrayList<Plugin> plugins = new ArrayList();

    public static abstract class Plugin {
        public String modid;

        public Plugin() {}

        public abstract void preInit();

        public abstract void init();

        public abstract void postInit();
    }

    static {
        //TODO: Future version
        //add("harvestcraft", HarvestCraft.class);
        add("bettersleeping", BetterSleeping.class);
    }

    private static void add(String modid, Class clazz) {
        if (Loader.isModLoaded(modid)) {
            try {
                Plugin plugin = (Plugin) clazz.newInstance();
                plugin.modid = modid;
                plugins.add(plugin);
            } catch (Exception e) {
                HarvestMoon.logger.log(Level.INFO, "Failed to load plugin for modid: " + modid);
            }
        }
    }

    public static void preInit() {
        for (Plugin plugin : plugins) {
            try {
                plugin.preInit();
            } catch (Exception e) {
                HarvestMoon.logger.log(Level.INFO, "Failed to load plugin for modid @ PreINIT: " + plugin.modid);
            }
        }
    }

    public static void init() {
        for (Plugin plugin : plugins) {
            try {
                plugin.init();
            } catch (Exception e) {
                HarvestMoon.logger.log(Level.INFO, "Failed to load plugin for modid @ INIT: " + plugin.modid);
            }
        }
    }

    public static void postInit() {
        for (Plugin plugin : plugins) {
            try {
                plugin.postInit();
            } catch (Exception e) {
                HarvestMoon.logger.log(Level.INFO, "Failed to load plugin for modid @ PostINIT: " + plugin.modid);
            }
        }

        //Clear out the plugins as we should no longer need them loaded
        plugins = null;
    }
}
