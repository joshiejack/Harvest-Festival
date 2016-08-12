package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.util.HFLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import static joshie.harvest.core.helpers.generic.ConfigHelper.setCategory;
import static joshie.harvest.core.helpers.generic.ConfigHelper.setConfig;
import static joshie.harvest.core.lib.HFModInfo.MODNAME;

public class HFCommonProxy {
    private static final List<Class> LIST = new ArrayList<>();
    private static final boolean ENABLE_LOGGING = true;

    static {
        //LIST.add(HFCore.class);
        //LIST.add(HFCalendar.class);
        //LIST.add(HFCrops.class);
        //LIST.add(HFNPCs.class); //After Crops
        //LIST.add(HFBuildings.class); //After NPCS
        //LIST.add(HFTools.class); //After Most Everything
        //LIST.add(HFCooking.class); //After Crops
        //LIST.add(HFIngredients.class); //After Cooking
        //LIST.add(HFRecipes.class); //After Ingredients
        //LIST.add(HFMining.class); //After Most Everything
        //LIST.add(HFGathering.class); //After Most Everything
        //LIST.add(HFGifts.class); //After Every Item is loaded
        //LIST.add(HFAnimals.class); //After Crops
        //LIST.add(HFShops.class); //After Most Everything
        //LIST.add(HFTab.class); //After Every Item is loaded
        //LIST.add(HFPlugins.class); //After Every Item is loaded
        //LIST.add(HFRecipeFixes.class); //After Every Item is loaded
    }

    public void setup(@Nonnull ASMDataTable table) {
        List<Pair<Integer, String>> unsorted = new ArrayList<>();
        Set<ASMData> datas = new HashSet<>(table.getAll(HFLoader.class.getCanonicalName()));
        for (ASMDataTable.ASMData data : datas) {
            try {
                String clazz = data.getClassName();
                Map<String, Object> map = data.getAnnotationInfo();
                int value = map.get("priority") != null? (int) map.get("priority"): 1;
                unsorted.add(Pair.of(value, clazz));
            } catch (Exception e) { e.printStackTrace(); }
        }

        //Now that we have gathered all the classes, let's sort them by priority
        Comparator<Pair<Integer, String>> priority = new Comparator<Pair<Integer, String>>() {
            @Override
            public int compare(Pair<Integer, String> str1, Pair<Integer, String> str2) {
                return str1.getLeft() < str2.getLeft() ? 1: str1.getLeft() > str2.getLeft() ? -1 : 0;
            }
        };

        Collections.sort(unsorted, priority);

        //Add Everything to the real LIST
        for (Pair<Integer, String> entry: unsorted) {
            try {
                LIST.add(Class.forName(entry.getRight()));
            } catch (Exception e) {}
        }
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

    public void clear() {
        LIST.clear();
    }

    public boolean isClient() {
        return false;
    }

    public void loadAPI(ASMDataTable data) {
        HFApiLoader.load(data, isClient());
    }
}
