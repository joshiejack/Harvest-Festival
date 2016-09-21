package joshie.harvest.npc.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.ISchedule;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.npc.schedule.ScheduleLocations.BARNLEFT;
import static joshie.harvest.npc.schedule.ScheduleLocations.PONDBACK;

public class ScheduleCandice implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, INPC npc, Season season, Weekday day, long time) {
        if (time >= 9000L && time <= 17000L) return BARNLEFT;
        else if (time >= 22000L && time <= 24000L) return PONDBACK;
        else return npc.getLocation(HOME);
    }
}
