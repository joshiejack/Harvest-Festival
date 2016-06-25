package joshie.harvest.core.util;

import joshie.harvest.calendar.WeatherProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

//STOLEN FROM THE ALLMIGHTY CHYLEX
public class WorldDestroyer {
    public static final DimensionType SEASONS = DimensionType.register("seasons", "seasons", 3, WeatherProvider.class, true);

    @SuppressWarnings("unchecked")
    public static void replaceWorldProvider() {
        DimensionManager.unregisterDimension(0);
        DimensionManager.registerDimension(0, SEASONS);
    }
}