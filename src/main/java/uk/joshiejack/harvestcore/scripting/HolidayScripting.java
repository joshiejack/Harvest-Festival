package uk.joshiejack.harvestcore.scripting;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Holiday;
import uk.joshiejack.seasons.date.CalendarDate;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldJS;
import uk.joshiejack.seasons.date.DateHelper;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class HolidayScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("holiday", HolidayScripting.class);
    }

    private static boolean is(World world, Holiday holiday) {
        CalendarDate date = DateHelper.getDate(world);
        return date.getDay() >= holiday.getDay() && date.getDay() <= holiday.getDay() + Holiday.LENGTH
                && holiday.isSeason(SeasonsSavedData.getWorldData(world).getSeason());
    }

    public static boolean is(WorldJS<?> worldJS) {
        return Holiday.REGISTRY.values().stream().anyMatch((h) -> is(worldJS.penguinScriptingObject, h));
    }
    public static boolean is(WorldJS<?> worldJS, String string) {
        return is(worldJS.penguinScriptingObject, Holiday.REGISTRY.get(new ResourceLocation(string)));
    }
}
