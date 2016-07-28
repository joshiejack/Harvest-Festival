package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.HFAnimals;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.HFIngredients;
import joshie.harvest.cooking.HFRecipes;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.debug.HFDebug;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.mining.HFMining;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.gift.init.HFGifts;
import joshie.harvest.plugins.HFPlugins;
import joshie.harvest.shops.HFShops;
import joshie.harvest.tools.HFTools;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.core.helpers.generic.ConfigHelper.setCategory;
import static joshie.harvest.core.helpers.generic.ConfigHelper.setConfig;
import static joshie.harvest.core.lib.HFModInfo.MODNAME;

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
        LIST.add(HFTools.class);
        LIST.add(HFCooking.class);
        LIST.add(HFIngredients.class);
        LIST.add(HFRecipes.class);
        LIST.add(HFShops.class);
        LIST.add(HFMining.class);
        LIST.add(HFGathering.class);
        LIST.add(HFGifts.class);
        LIST.add(HFAnimals.class);
        LIST.add(HFTab.class);
        LIST.add(HFRecipeFixes.class);
    }

    public void configure(File file) {
        Configuration config = new Configuration(file);
        for (Class c : LIST) {
            try {
                Method configure = c.getMethod("configure");
                if (configure != null) {
                    String name = c.getSimpleName().replace("HF", "");
                    try {
                        config.load();
                        setConfig(config);
                        setCategory(name);
                        configure.invoke(null);
                    } catch (Exception e) {
                        HarvestFestival.LOGGER.log(Level.ERROR, MODNAME + " failed to load in it's " + name + " config");
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

    public void loadAPI(ASMDataTable data) {
        HFApiLoader.load(data, isClient());
    }
}
