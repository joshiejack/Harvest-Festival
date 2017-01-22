package joshie.harvest.npcs.schedule;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Holiday;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.ScheduleBuilder;
import joshie.harvest.api.npc.ScheduleBuilder.HolidaySchedule;
import joshie.harvest.api.npc.ScheduleBuilder.TimedSchedule;
import joshie.harvest.calendar.HolidayRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.TreeMap;

public class Schedule implements ISchedule {
    private final TreeMap<Season, TreeMap<Weekday, TreeMap<Long, BuildingLocation>>> seasonalMap = new TreeMap<>();
    private final HashMap<Holiday, TreeMap<Long, BuildingLocation>> holidayMap = new HashMap<>();
    private final BuildingLocation default_;

    public Schedule(ScheduleBuilder builder) {
        this.default_ = builder.default_;
        for (TimedSchedule time: builder.timedScheduleList) register(time.season, time.weekday, time.time, time.location);
        for (HolidaySchedule time: builder.holidayScheduleList) register(time.holiday, time.time, time.location);
    }

    public void register(Holiday holiday, long time, BuildingLocation location) {
        TreeMap<Long, BuildingLocation> hourlyMap = getOrCreateMap(holidayMap, holiday);
        hourlyMap.put(time, location);
        holidayMap.put(holiday, hourlyMap);
    }

    private TreeMap<Long, BuildingLocation> getOrCreateMap(HashMap<Holiday, TreeMap<Long, BuildingLocation>> holidayMap, Holiday holiday) {
        return holidayMap.containsKey(holiday) ? holidayMap.get(holiday) : new TreeMap<>();
    }

    public void register(Season season, Weekday weekday, long time, BuildingLocation location) {
        TreeMap<Weekday, TreeMap<Long, BuildingLocation>> weekdayMap = getOrCreateMap(season);
        TreeMap<Long, BuildingLocation> hourlyMap = getOrCreateMap(weekdayMap, weekday);
        hourlyMap.put(time, location);
        weekdayMap.put(weekday, hourlyMap);
        seasonalMap.put(season, weekdayMap);
    }

    private TreeMap<Long, BuildingLocation> getOrCreateMap(TreeMap<Weekday, TreeMap<Long, BuildingLocation>> map, Weekday weekday) {
        return map.containsKey(weekday) ? map.get(weekday) : new TreeMap<>();
    }

    private TreeMap<Weekday, TreeMap<Long, BuildingLocation>> getOrCreateMap(Season season) {
        return seasonalMap.containsKey(season) ? seasonalMap.get(season) : new TreeMap<>();
    }

    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, NPC npc, Season season, Weekday weekday, long time) {
        if (default_ == null) return null;
        //Holidays take priority over anything else
        Holiday holiday = HolidayRegistry.INSTANCE.getHoliday(world, new BlockPos(entity), HFApi.calendar.getDate(world));
        if (!holiday.equals(HolidayRegistry.NONE) && holidayMap.containsKey(holiday)) {
            BuildingLocation location = getTargetFromMapBasedOnTime(holidayMap.get(holiday), time);
            if (location != null) return location;
        }

        //Now we stick to normal schedules
        Season seasonal = seasonalMap.lowerKey(season) == null ? season: seasonalMap.lowerKey(season);
        if (seasonalMap.containsKey(seasonal)) {
            TreeMap<Weekday, TreeMap<Long, BuildingLocation>> weekdayMap = seasonalMap.get(seasonal);
            Weekday weekly = weekdayMap.lowerKey(weekday) == null ? weekday : weekdayMap.lowerKey(weekday);
            if (weekdayMap.containsKey(weekly)) {
                BuildingLocation location = getTargetFromMapBasedOnTime(weekdayMap.get(weekly), time);
                if (location != null) return location;
            }
        }

        return default_;
    }

    @Nullable
    private BuildingLocation getTargetFromMapBasedOnTime(TreeMap<Long, BuildingLocation> hourlyMap, long time) {
        long Ttime = hourlyMap.lowerKey(time) == null ? time : hourlyMap.lowerKey(time);
        if (hourlyMap.containsKey(Ttime)) {
            BuildingLocation location = hourlyMap.get(Ttime);
            if (location != null) return location;
        }

        return null;
    }
}
