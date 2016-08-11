package joshie.harvest.npc.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.ISchedule;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.api.npc.INPC.Location.WORK;
import static joshie.harvest.npc.schedule.ScheduleLocations.CARPENTERFRONT;
import static joshie.harvest.npc.schedule.ScheduleLocations.CARPENTERUP;

public class ScheduleYulife implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, INPC npc, Season season, Weekday day, long time) {
        if (npc.getShop() != null && npc.getShop().isOpen(world, null)) return npc.getLocation(WORK);
        else {
            if (time >= 9000L && time <= 17000L) return CARPENTERFRONT;
            else if (time >= 22000L && time <= 24000L) return CARPENTERUP;
            else if (time >= 0L && time <= 5000L) return CARPENTERUP;
            else return npc.getLocation(HOME);
        }
    }
}
