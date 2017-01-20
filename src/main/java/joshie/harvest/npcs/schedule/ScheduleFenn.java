package joshie.harvest.npcs.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.calendar.Weekday.SUNDAY;
import static joshie.harvest.api.npc.NPC.Location.HOME;
import static joshie.harvest.town.BuildingLocations.*;

@SuppressWarnings("unused")
public class ScheduleFenn implements ISchedule {
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, NPC npc, Season season, Weekday day, long time) {
        if (time >= 8000L && time <= 10000L) return day == SUNDAY ? CHURCHPEWBACKLEFT : BARN_DOOR;
        else if (time >= 10000L && time <= 11000L) return POULTRY_DOOR;
        else if (time >= 12000L && time <= 15000L) return TOWNHALLLEFT;
        else if (time >= 16000L && time <= 18000L) return FISHING_POND_LEFT;
        else return npc.getLocation(HOME);
    }
}
