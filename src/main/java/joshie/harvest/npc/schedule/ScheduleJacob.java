package joshie.harvest.npc.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.buildings.HFBuildings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.api.npc.INPC.Location.WORK;
import static joshie.harvest.town.TownData.FISHING_POND;

public class ScheduleJacob implements ISchedule {
    private static final BuildingLocation POND = new BuildingLocation(HFBuildings.FISHING_HOLE, FISHING_POND);

    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, INPC npc, long time) {
        if (npc.getShop() != null && npc.getShop().isOpen(world, null)) return npc.getLocation(WORK);
        if (time >= 5000L && time <= 9000L) return new BuildingLocation(HFBuildings.FISHING_HOLE, FISHING_POND);
        else return npc.getLocation(HOME);
    }
}
