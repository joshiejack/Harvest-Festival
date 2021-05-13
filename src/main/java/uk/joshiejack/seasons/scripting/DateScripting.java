package uk.joshiejack.seasons.scripting;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingBindings;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldJS;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.date.DateHelper;

import java.time.DayOfWeek;

@Mod.EventBusSubscriber(modid = Seasons.MODID)
public class DateScripting {
    @SubscribeEvent
    public static void onCollectBindings(CollectScriptingBindings event) {
        event.registerEnum(DayOfWeek.class);
    }

    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("calendar", DateScripting.class);
    }

    public static int day(WorldJS<?> worldJS) { return DateHelper.getDate(worldJS.penguinScriptingObject).getDay(); }

    public static DayOfWeek weekday(WorldJS<?> worldJS) {
        return DateHelper.getDate(worldJS.penguinScriptingObject).getWeekday();
    }

    public static int year(WorldJS<?> worldJS) {
        return DateHelper.getDate(worldJS.penguinScriptingObject).getYear();
    }

    public static int elapsed(WorldJS<?> world) { return TimeHelper.getElapsedDays(world.penguinScriptingObject); }
}
