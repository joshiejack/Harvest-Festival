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
import static joshie.harvest.api.npc.INPC.Location.WORK;
import static joshie.harvest.npc.schedule.ScheduleLocations.*;

public class ScheduleLiara implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, INPC npc, Season season, Weekday day, long time) {
        if (npc.getShop() != null && npc.getShop().isOpen(world, null)) return npc.getLocation(WORK);
        else {
            if (time >= 5000L && time <= 7000L) return POND;
            else if (time >= 7000L && time <= 17000L) return day == SUNDAY ? CHURCHINSIDE : CAFEFRONT;
            else if (time >= 20000L && time <= 22000L) return GODDESS;
            else return npc.getLocation(HOME);
        }
    }
}
