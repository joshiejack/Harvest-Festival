package uk.joshiejack.seasons.scripting;

import uk.joshiejack.penguinlib.scripting.event.CollectScriptingBindings;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldJS;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.date.DateHelper;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Seasons.MODID)
public class SeasonScripting {
    @SubscribeEvent
    public static void onCollectBindings(CollectScriptingBindings event) {
        event.registerEnum(Season.class);
    }

    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("season", SeasonScripting.class);
    }

    public static Season fromID(int id) {
        return ArrayHelper.getArrayValue(Season.MAIN, id);
    }

    public static Season get(WorldJS<?> worldJS) {
        return SeasonsSavedData.getWorldData(worldJS.penguinScriptingObject).getSeason();
    }

    public static boolean is(WorldJS<?> worldJS, int id) {
        return get(worldJS) == ArrayHelper.getArrayValue(Season.MAIN, id);
    }

    public static int asYear(long time) {
        return DateHelper.getYear(time);
    }
}
