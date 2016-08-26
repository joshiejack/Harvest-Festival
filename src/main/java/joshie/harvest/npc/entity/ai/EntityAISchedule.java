package joshie.harvest.npc.entity.ai;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.api.npc.INPC.Location.WORK;

public class EntityAISchedule extends EntityAIBase {
    private EntityNPC npc;
    private long attemptTimer;
    private BuildingLocation location;
    private BlockPos target;

    public EntityAISchedule(EntityNPC npc) {
        this.npc = npc;
        this.setMutexBits(3);
    }

    private BuildingLocation getLocation(ICalendarDate date) {
        if (npc.getNPC().getShop() != null && npc.getNPC().getShop().isOpen(npc.worldObj, null)) return npc.getNPC().getLocation(WORK);
        else return npc.getNPC().getScheduler().getTarget(npc.worldObj, npc, npc.getNPC(), date.getSeason(), date.getWeekday(), CalendarHelper.getTime(npc.worldObj));
    }

    @Override
    public boolean shouldExecute() {
        ICalendarDate date = HFTrackers.getCalendar(npc.worldObj).getDate();
        location = getLocation(date);
        target = NPCHelper.getCoordinatesForLocation(npc, location);
        attemptTimer = 0L;
        return target != null && npc.getDistanceSq(target) > 20D;
    }

    @Override
    public boolean continueExecuting() {
        attemptTimer++;
        if (attemptTimer >= 2500L) {
            npc.setPositionAndUpdate(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);
            attemptTimer = 0L;
            return false;
        }

        if (target == null) return false;
        return npc.getDistanceSq(target) > 3D;
    }

    @Override
    public void startExecuting() {
        ICalendarDate date = HFTrackers.getCalendar(npc.worldObj).getDate();
        location = getLocation(date);
        target = NPCHelper.getCoordinatesForLocation(npc, location);
        if (target != null) {
            if (attemptTimer < 2500L) {
                npc.getNavigator().tryMoveToXYZ((double) target.getX() + 0.5D, (double) target.getY(), (double) target.getZ() + 0.5D, 0.5D);
            } else npc.setPositionAndUpdate(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);

            attemptTimer++;
        }
    }
}
