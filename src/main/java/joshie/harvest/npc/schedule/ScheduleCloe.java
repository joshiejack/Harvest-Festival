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

public class ScheduleCloe implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, INPC npc, Season season, Weekday day, long time) {
        if (time >= 7000L && time <= 10000L) return day == SUNDAY ? CHURCHPEWFRONTRIGHT : CAFEKITCHEN;
        else if (time >= 10000L && time <= 11000L) return BARNRIGHT;
        else if (time >= 12000L && time <= 15000L) return TOWNHALLRIGHT;
        else return npc.getLocation(HOME);
    }
}
