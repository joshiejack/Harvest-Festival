package joshie.harvest.npc.entity.ai;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAISchedule extends EntityAIBase {
    private AbstractEntityNPC npc;
    private long attemptTimer;
    private BuildingLocation location;
    private BlockPos target;

    public EntityAISchedule(AbstractEntityNPC npc) {
        this.npc = npc;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        ICalendarDate date = HFTrackers.getCalendar(npc.worldObj).getDate();
        location = npc.getNPC().getScheduler().getTarget(npc.worldObj, npc, npc.getNPC(), date.getSeason(), date.getWeekday(), CalendarHelper.getTime(npc.worldObj));
        target = NPCHelper.getCoordinatesForLocation(npc, location);
        attemptTimer = 0L;
        return target != null && npc.getDistanceSq(target) > 16D;
    }

    @Override
    public boolean continueExecuting() {
        attemptTimer++;
        if (attemptTimer >= 500) {
            npc.setPositionAndUpdate(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);
            attemptTimer = 0L;
            return false;
        }

        return npc.getDistanceSq(target) > 1D;
    }

    @Override
    public void startExecuting() {
        ICalendarDate date = HFTrackers.getCalendar(npc.worldObj).getDate();
        location = npc.getNPC().getScheduler().getTarget(npc.worldObj, npc, npc.getNPC(), date.getSeason(), date.getWeekday(), CalendarHelper.getTime(npc.worldObj));
        target = NPCHelper.getCoordinatesForLocation(npc, location);
        if (target != null) {
            if (attemptTimer < 1000L) {
                npc.getNavigator().tryMoveToXYZ((double) target.getX() + 0.5D, (double) target.getY(), (double) target.getZ() + 0.5D, 0.5D);
            } else npc.setPositionAndUpdate(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);

            attemptTimer++;
        }
    }
}
