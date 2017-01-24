package joshie.harvest.calendar;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
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
    private final HashMap<Festival, CalendarDate> holidays = new HashMap<>();
    public static final Festival NONE = new Festival(new ResourceLocation(MODID, "none"));

    boolean isHoliday(CalendarDate date) {
        for (CalendarDate holiday : holidays.values()) {
            if (holiday.isSameDay(date)) return true;
        }

        return false;
    }

    @Nonnull
    public Festival getHoliday(CalendarDate date) {
        for (Entry<Festival, CalendarDate> entry : holidays.entrySet()) {
            if (entry.getValue().isSameDay(date)) {
                return entry.getKey();
            }
        }

        return NONE;
    }

    @Nonnull
    public Festival getHoliday(World world, BlockPos pos, CalendarDate date) {
        for (Entry<Festival, CalendarDate> entry : holidays.entrySet()) {
            if (entry.getValue().isSameDay(date)) {
                Festival festival = entry.getKey();
                if (festival.getQuest() == null ||
                        (festival.getQuest() != null && !TownHelper.getClosestTownToBlockPos(world, pos).getQuests().getFinished().contains(festival.getQuest()))) {
                    return festival;
                }
            }
        }

        return NONE;
    }

    public void register(Festival festival, CalendarDate date) {
        holidays.put(festival, date);
    }
}
