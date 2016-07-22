package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.HFAnimals;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.HFIngredients;
import joshie.harvest.cooking.HFRecipes;
import joshie.harvest.core.helpers.generic.ConfigHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.debug.HFDebug;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.items.HFItems;
import joshie.harvest.mining.HFMining;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.gift.init.HFGifts;
import joshie.harvest.plugins.HFPlugins;
import joshie.harvest.quests.HFQuests;
import joshie.harvest.shops.HFShops;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HFCommonProxy {
    private static final List<Class> LIST = new ArrayList<>();
    private static final boolean ENABLE_LOGGING = false;

    static {
        if (HFCore.DEBUG_MODE) {
            LIST.add(HFDebug.class);
        }

        LIST.add(HFCore.class);
        LIST.add(HFPlugins.class);
        LIST.add(HFCalendar.class);
        LIST.add(HFCrops.class);
        LIST.add(HFNPCs.class);
        LIST.add(HFBuildings.class);
        LIST.add(HFBlocks.class);
        LIST.add(HFItems.class);
        LIST.add(HFCooking.class);
        LIST.add(HFIngredients.class);
        LIST.add(HFRecipes.class);
        LIST.add(HFQuests.class);
        LIST.add(HFShops.class);
        LIST.add(HFMining.class);
        LIST.add(HFGathering.class);
        LIST.add(HFGifts.class);
        LIST.add(HFAnimals.class);
        LIST.add(HFTab.class);
        LIST.add(HFRecipeFixes.class);
    }

    public void configure() {
        for (Class c : LIST) {
            try {
                Method configure = c.getMethod("configure");
                if (configure != null) {
                    String name = c.getSimpleName().replace("HF", "");
                    Configuration config = new Configuration(new File(HarvestFestival.root, name.replaceAll("(.)([A-Z])", "$1-$2").toLowerCase() + ".cfg"));
                    try {
                        config.load();
                        ConfigHelper.setConfig(config);
                        configure.invoke(null);
                    } catch (Exception e) {
                        HarvestFestival.LOGGER.log(Level.ERROR, HFModInfo.MODNAME + " failed to load in it's " + name + " config");
                        e.printStackTrace();
                    } finally {
                        config.save();
                    }
                }
            } catch (Exception e) {}
        }
    }

    public void load(String stage) {
        //Continue
        for (Class c : LIST) {
            try { //Attempt to load default
                c.getMethod(stage).invoke(null);
            } catch (NoClassDefFoundError | NoSuchMethodException e) { }
              catch (Exception e) {
                 if (ENABLE_LOGGING) e.printStackTrace();
             }

            //Attempt to load client side only
            if (isClient()) {
                try { //Attempt to load default
                    c.getMethod(stage + "Client").invoke(null);
                } catch (NoClassDefFoundError | NoSuchMethodException e) { }
                catch (Exception e) {
                    if (ENABLE_LOGGING) e.printStackTrace();
                }
            }
        }
    }

    public boolean isClient() {
        return false;
    }
}
