package joshie.harvest.api.npc;

import joshie.harvest.api.buildings.BuildingLocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.api.npc.INPC.Location.WORK;

/** This is the schedule for npcs **/
public interface ISchedule {
    /** Return the building target for this npc
     *
     * @param world     the world
     * @param entity    the entity
     * @param npc       the npc
     * @param time      the current time, scaled from 0-23999 In real time, so 0 = midnight, 6000 = 6am
     * @return the building */
    default BuildingLocation getTarget(World world, EntityLiving entity, INPC npc, long time) {
        if (npc.getShop() != null && npc.getShop().isOpen(world, null)) return npc.getLocation(WORK);
        else {
            return npc.getLocation(HOME);
        }
    }
}
