package joshie.harvest.calendar;

import joshie.harvest.api.calendar.CalendarDate;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map.Entry;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class HolidayRegistry {
    public static final HolidayRegistry INSTANCE = new HolidayRegistry();
    private final HashMap<ResourceLocation, CalendarDate> holidays = new HashMap<>();
    public static final ResourceLocation NONE = new ResourceLocation(MODID, "none");

    boolean isHoliday(CalendarDate date) {
        for (CalendarDate holiday : holidays.values()) {
            if (holiday.isSameDay(date)) return true;
        }

        return false;
    }

    @Nonnull
    public ResourceLocation getHoliday(CalendarDate date) {
        for (Entry<ResourceLocation, CalendarDate> entry : holidays.entrySet()) {
            if (entry.getValue().isSameDay(date)) return entry.getKey();
        }

        return NONE;
    }

    public void register(ResourceLocation name, CalendarDate date) {
        holidays.put(name, date);
    }
}
