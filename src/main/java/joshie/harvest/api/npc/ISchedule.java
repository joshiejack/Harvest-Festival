package joshie.harvest.api.npc;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/** This is the schedule for npcs **/
public interface ISchedule {
    /** Return the building target for this npc
     *
     *
     * @param world     the world
     * @param entity    the entity
     * @param npc       the npc
     * @param season    the season
     * @param day       the day of the week
     * @param time      the current time, scaled from 0-23999 In real time, so 0 = midnight, 6000 = 6am
     * @return the building */
    @Nullable
    BuildingLocation getTarget(World world, EntityLiving entity, NPC npc, @Nullable Season season, Weekday day, long time);
}
