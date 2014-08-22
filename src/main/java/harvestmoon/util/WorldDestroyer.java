package harvestmoon.util;

import harvestmoon.calendar.WeatherProvider;

import java.lang.reflect.Field;
import java.util.Hashtable;

import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

//STOLEN FROM THE ALLMIGHTY CHYLEX
public class WorldDestroyer {
    @SuppressWarnings("unchecked")
    public static void replaceWorldProvider() {
        try {
            Field f = DimensionManager.class.getDeclaredField("providers");
            f.setAccessible(true); // let it throw NPE if the field isn't found
            Hashtable<Integer,Class<? extends WorldProvider>> providers = (Hashtable<Integer, Class<? extends WorldProvider>>) f.get(null);
            providers.put(0, WeatherProvider.class);
            f.set(null, providers);
        } catch (Exception e) {
            throw new RuntimeException("Could not override the DimensionManager providers!", e);
        }
    }
}
