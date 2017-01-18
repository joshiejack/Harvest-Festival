package joshie.harvest.npcs.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.NPC.Location.HOME;
import static joshie.harvest.town.BuildingLocations.GODDESSFRONT;
import static joshie.harvest.town.BuildingLocations.POND;

@SuppressWarnings("unused")
public class ScheduleJacob implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, NPC npc, Season season, Weekday weekday, long time) {
        if (time >= 5000L && time <= 9000L) return POND;
        else if (time >= 9000L && time <= 11000L) return GODDESSFRONT;
        else return npc.getLocation(HOME);
    }
}
