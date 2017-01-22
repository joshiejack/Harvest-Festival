package joshie.harvest.calendar;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Holiday;
import joshie.harvest.town.TownHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map.Entry;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class HolidayRegistry {
    public static final HolidayRegistry INSTANCE = new HolidayRegistry();
    private final HashMap<Holiday, CalendarDate> holidays = new HashMap<>();
    public static final Holiday NONE = new Holiday(new ResourceLocation(MODID, "none"), null);

    boolean isHoliday(CalendarDate date) {
        for (CalendarDate holiday : holidays.values()) {
            if (holiday.isSameDay(date)) return true;
        }

        return false;
    }

    @Nonnull
    public Holiday getHoliday(CalendarDate date) {
        for (Entry<Holiday, CalendarDate> entry : holidays.entrySet()) {
            if (entry.getValue().isSameDay(date)) {
                return entry.getKey();
            }
        }

        return NONE;
    }

    @Nonnull
    public Holiday getHoliday(World world, BlockPos pos, CalendarDate date) {
        for (Entry<Holiday, CalendarDate> entry : holidays.entrySet()) {
            if (entry.getValue().isSameDay(date)) {
                Holiday holiday = entry.getKey();
                if (holiday.getQuest() == null ||
                        (holiday.getQuest() != null && !TownHelper.getClosestTownToBlockPos(world, pos).getQuests().getFinished().contains(holiday.getQuest()))) {
                    return holiday;
                }
            }
        }

        return NONE;
    }

    public void register(Holiday holiday, CalendarDate date) {
        holidays.put(holiday, date);
    }
}
