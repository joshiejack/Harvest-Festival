package joshie.harvest.npc.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.ISchedule;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.INPC.Location.HOME;

public class ScheduleBrandon implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, INPC npc, Season season, Weekday day, long time) {
        return npc.getLocation(HOME);
    }
}
