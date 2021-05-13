package uk.joshiejack.husbandry.scripting;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.scripting.wrappers.AnimalStatsJS;
import uk.joshiejack.penguinlib.events.CollectScriptingWrappers;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.EntityJS;

import static uk.joshiejack.husbandry.Husbandry.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class AnimalScripting {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("animals", AnimalScripting.class);
    }

    @SubscribeEvent
    public static void onCollectWrappers(CollectScriptingWrappers event) {
        event.register(AnimalStatsJS.class, AnimalStats.class);
    }

    public static AnimalStatsJS stats(EntityJS<?> entityJS) {
        return WrapperRegistry.wrap(AnimalStats.getStats(entityJS.penguinScriptingObject));
    }

    public static void addHappiness(EntityJS<?> entityW, int value) {
        AnimalStats<?> stats = AnimalStats.getStats(entityW.penguinScriptingObject);
        if (stats != null) {
            stats.increaseHappiness(value);
        }
    }

    public static int getHeartLevel(EntityJS<?> entityW) {
        AnimalStats<?> stats = AnimalStats.getStats(entityW.penguinScriptingObject);
        return stats == null ? 0 : stats.getHearts();
    }
}
