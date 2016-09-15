package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.util.HFLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import static joshie.harvest.core.helpers.ConfigHelper.setCategory;
import static joshie.harvest.core.helpers.ConfigHelper.setConfig;
import static joshie.harvest.core.lib.HFModInfo.MODNAME;

public class HFCommonProxy {
    private static final List<Class> LIST = new ArrayList<>();
    private static final boolean ENABLE_LOGGING = true;

    public void setup(@Nonnull ASMDataTable table) {
        List<Triple<Integer, String, String>> unsorted = new ArrayList<>();
        Set<ASMData> datas = new HashSet<>(table.getAll(HFLoader.class.getCanonicalName()));
        for (ASMDataTable.ASMData data : datas) {
            try {
                String clazz = data.getClassName();
                Map<String, Object> map = data.getAnnotationInfo();
                String mods = map.get("mods") != null ? (String) map.get("mods") : "";
                int value = mods.equals("") ? map.get("priority") != null? (int) map.get("priority"): 1: -5000;
                unsorted.add(Triple.of(value, mods, clazz));
            } catch (Exception e) { e.printStackTrace(); }
        }

        //Now that we have gathered all the classes, let's sort them by priority
        Comparator<Triple<Integer, String, String>> priority = new Comparator<Triple<Integer, String, String>>() {
            @Override
            public int compare(Triple<Integer, String, String> str1, Triple<Integer, String, String> str2) {
                return str1.getLeft() < str2.getLeft() ? 1: str1.getLeft() > str2.getLeft() ? -1 : 0;
            }
        };

        Collections.sort(unsorted, priority);

        //Add Everything to the real LIST
        triple:
        for (Triple<Integer, String, String> entry: unsorted) {
            try {
                if (!entry.getMiddle().equals("")) {
                    String[] mods = entry.getMiddle().replace(" ", "").split(",");
                    for (String mod : mods) {
                        if (!isModLoaded(mod)) continue triple;
                    }
                }

                LIST.add(Class.forName(entry.getRight()));
            } catch (Exception e) {
                HarvestFestival.LOGGER.log(Level.ERROR, "Harvest Festival failed to load the following class: " + entry.getMiddle());
                HarvestFestival.LOGGER.log(Level.ERROR, "If this a mod related class, try updating your version of that mod before reporting");
            }
        }
    }
    private boolean isModLoaded(String mod) {
        return Loader.isModLoaded(mod) || Loader.isModLoaded(mod.toLowerCase(Locale.ENGLISH));
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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
