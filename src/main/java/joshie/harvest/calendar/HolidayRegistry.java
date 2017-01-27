package joshie.harvest.calendar;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.town.TownHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map.Entry;

public class HolidayRegistry {
    public static final HolidayRegistry INSTANCE = new HolidayRegistry();
    private final HashMap<Festival, CalendarDate> holidays = new HashMap<>();

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

        return Festival.NONE;
    }

    public void register(Festival festival, CalendarDate date) {
        holidays.put(festival, date);
    }
}
