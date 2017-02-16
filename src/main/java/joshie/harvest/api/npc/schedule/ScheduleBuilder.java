package joshie.harvest.api.npc.schedule;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.NPC;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ScheduleBuilder {
    public List<TimedSchedule> timedScheduleList = new ArrayList<>();
    public List<HolidaySchedule> holidayScheduleList = new ArrayList<>();
    public final BuildingLocation default_;
    private final NPC npc;

    private ScheduleBuilder(NPC npc, @Nullable BuildingLocation default_) {
        this.npc = npc;
        this.default_ = default_;
        this.npc.setHome(default_);
        MinecraftForge.EVENT_BUS.post(new ScheduleEvent.Pre(npc, this));
    }

    public static ScheduleBuilder create(NPC npc, @Nullable BuildingLocation default_) {
        return new ScheduleBuilder(npc, default_);
    }

    public ScheduleBuilder add(Season season, Weekday weekday, long time, BuildingLocation location) {
        timedScheduleList.add(new TimedSchedule(season, weekday, time, location));
        return this;
    }

    public ScheduleBuilder add(Festival festival, long time, BuildingLocation location) {
        holidayScheduleList.add(new HolidaySchedule(festival, time, location));
        return this;
    }

    public void build() {
        MinecraftForge.EVENT_BUS.post(new ScheduleEvent.Post(npc, this));
        npc.setScheduleHandler(HFApi.npc.buildSchedule(this));
    }

    //For the normal everyday schedule
    public static class TimedSchedule {
        public Season season;
        public Weekday weekday;
        public long time;
        public BuildingLocation location;

        TimedSchedule(Season season, Weekday weekday, long time, BuildingLocation location) {
            this.season = season;
            this.weekday = weekday;
            this.time = time;
            this.location = location;
        }
    }

    //For special schedules on specific holidays
    public static class HolidaySchedule {
        public Festival festival;
        public long time;
        public BuildingLocation location;

        HolidaySchedule(Festival festival, long time, BuildingLocation location) {
            this.festival = festival;
            this.time = time;
            this.location = location;
        }
    }
}
