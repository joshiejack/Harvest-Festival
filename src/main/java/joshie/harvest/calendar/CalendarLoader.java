package joshie.harvest.calendar;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@HFEvents
public class CalendarLoader {
    private static final String CALENDAR_NAME = HFModInfo.CAPNAME + "-Calendar";
    public static CalendarData data;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            //Load this data on one side only
            if (world.provider.getDimension() == 0) {
                data = (CalendarData) world.getPerWorldStorage().getOrLoadData(CalendarData.class, CALENDAR_NAME);
                if (data == null) {
                    data = new CalendarData(CALENDAR_NAME);
                    world.getPerWorldStorage().setData(CALENDAR_NAME, data);
                }

                data.getCalendar().setWorld(world);
                data.getCalendar().recalculate(world);
                HFTrackers.setServerCalendar(data.getCalendar());
            }
        }
    }
}
