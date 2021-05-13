package uk.joshiejack.seasons.world.weather;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingBindings;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldJS;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;

@Mod.EventBusSubscriber(modid = Seasons.MODID)
public class WeatherScripting {
    @SubscribeEvent
    public static void onCollectBindings(CollectScriptingBindings event) {
        event.registerEnum(Weather.class);
    }

    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("weather", WeatherScripting.class);
    }

    public static Weather tomorrow(WorldJS<?> worldJS) { return SeasonsSavedData.getWorldData(worldJS.penguinScriptingObject).getForecast(); }

    public static Weather today(WorldJS<?> worldJS) {
        return SeasonsSavedData.getWorldData(worldJS.penguinScriptingObject).getWeather();
    }
}
