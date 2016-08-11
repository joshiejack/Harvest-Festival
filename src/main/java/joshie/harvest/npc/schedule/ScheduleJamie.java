package joshie.harvest.npc.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.ISchedule;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.calendar.Weekday.SUNDAY;
import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.npc.schedule.ScheduleLocations.*;

public class ScheduleJamie implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, INPC npc, Season season, Weekday day, long time) {
        if (time >= 5000L && time <= 7000L) return TOWNHALLLEFT;
        else if (time >= 7000L && time <= 10000L) return day == SUNDAY ? CHURCHINSIDE : POULTRYBUILDING;
        else if (time >= 10000L && time <= 13000L) return TOWNHALLRIGHT;
        else if (time >= 13000L && time <= 17000L) return TOWNHALLSTAGE;
        else return npc.getLocation(HOME);
    }
}
