package joshie.harvest.api.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.schedule.ScheduleEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class ScheduleBuilder {
    public List<TimedSchedule> timedScheduleList = new ArrayList<>();
    public List<HolidaySchedule> holidayScheduleList = new ArrayList<>();
    public final BuildingLocation default_;
    private final NPC npc;

    private ScheduleBuilder(NPC npc, BuildingLocation default_) {
        this.npc = npc;
        this.default_ = default_;
        this.npc.setHome(default_);
        MinecraftForge.EVENT_BUS.post(new ScheduleEvent.Pre(npc, this));
    }

    public static ScheduleBuilder create(NPC npc, BuildingLocation default_) {
        return new ScheduleBuilder(npc, default_);
    }

    public ScheduleBuilder add(Season season, Weekday weekday, long time, BuildingLocation location) {
        timedScheduleList.add(new TimedSchedule(season, weekday, time, location));
        return this;
    }

    public ScheduleBuilder add(ResourceLocation holiday, long time, BuildingLocation location) {
        holidayScheduleList.add(new HolidaySchedule(holiday, time, location));
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

        public TimedSchedule(Season season, Weekday weekday, long time, BuildingLocation location) {
            this.season = season;
            this.weekday = weekday;
            this.time = time;
            this.location = location;
        }
    }

    //For special schedules on specific holidays
    public static class HolidaySchedule {
        public ResourceLocation holiday;
        public long time;
        public BuildingLocation location;

        public HolidaySchedule(ResourceLocation holiday, long time, BuildingLocation location) {
            this.holiday = holiday;
            this.time = time;
            this.location = location;
        }
    }
}
