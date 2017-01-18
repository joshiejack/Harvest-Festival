package joshie.harvest.npcs.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.NPC.Location.HOME;
import static joshie.harvest.town.BuildingLocations.*;

@SuppressWarnings("unused")
public class ScheduleDaniel implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, NPC npc, Season season, Weekday day, long time) {
        if (time >= 5000L && time <= 9000L) return CAFEFRONT;
        else if (time >= 9000L && time <= 11000L) return GENERALFRONT;
        else if (time >= 15000L && time <= 19000L) return TOWNHALLENTRANCE;
        else return npc.getLocation(HOME);
    }
}
