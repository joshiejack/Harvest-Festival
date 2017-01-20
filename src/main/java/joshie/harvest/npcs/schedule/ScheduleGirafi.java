package joshie.harvest.npcs.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.town.BuildingLocations.*;

@SuppressWarnings("unused")
public class ScheduleGirafi implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, NPC npc, Season season, Weekday day, long time) {
        if (time >= 9000L && time <= 17000L) return GENERAL_CUSTOMER;
        else if (time >= 23000L && time <= 24000L) return GODDESS_POND_BACK_RIGHT;
        else return TOWNHALL_RIGHT;
    }
}
