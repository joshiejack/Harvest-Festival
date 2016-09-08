package joshie.harvest.npc.entity.ai;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.npc.NPCHelper;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static joshie.harvest.api.npc.INPC.Location.WORK;

public class EntityAISchedule extends EntityAIBase {
    private EntityNPC npc;
    private long attemptTimer;
    private BuildingLocation buildingTarget;
    private BlockPos blockTarget;
    private Vec3d tempTarget;

    public EntityAISchedule(EntityNPC npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    private BuildingLocation getBuildingTarget(CalendarDate date) { //If the npcs shop is open, they should try to go their job
        if (NPCHelper.isShopPreparingToOpen(npc.getNPC(), npc.worldObj)) return npc.getNPC().getLocation(WORK);
        //Otherwise they will try to follow their own schedule
        return npc.getNPC().getScheduler().getTarget(npc.worldObj, npc, npc.getNPC(), date.getSeason(), date.getWeekday(), CalendarHelper.getTime(npc.worldObj));
    }

    @Override
    public boolean shouldExecute() {
        CalendarDate date = HFApi.calendar.getDate(npc.worldObj);
        buildingTarget = getBuildingTarget(date);
        blockTarget = NPCHelper.getCoordinatesForLocation(npc, buildingTarget);
        attemptTimer = 0L;
        return blockTarget != null && npc.getDistanceSq(blockTarget) > buildingTarget.getDistanceRequired();
    }

    @Override
    public boolean continueExecuting() {
        return blockTarget != null && npc.getDistanceSq(blockTarget) > buildingTarget.getDistanceRequired();
    }

    @Override
    public void updateTask() {
        //Every 10 seconds, update our target
        if (attemptTimer %200 == 0) {
            BuildingLocation previous = buildingTarget;
            CalendarDate date = HFApi.calendar.getDate(npc.worldObj);
            buildingTarget = getBuildingTarget(date);
            blockTarget = NPCHelper.getCoordinatesForLocation(npc, buildingTarget);
            if (blockTarget != null) {
                tempTarget = RandomPositionGenerator.findRandomTargetBlockTowards(npc, 5, 3, new Vec3d((double) blockTarget.getX() + 0.5D, (double) blockTarget.getY(), (double) blockTarget.getZ() + 0.5D));
            }

            if (!previous.equals(buildingTarget)) attemptTimer = 0; //Reset the attempt timer, if this is a new building
        }

        //If our new target is valid, continue executing
        if (tempTarget != null) {
            if (attemptTimer < buildingTarget.getTimeToTry()) {
                npc.getNavigator().tryMoveToXYZ(tempTarget.xCoord, tempTarget.yCoord, tempTarget.zCoord, 0.5D);
            } else {
                npc.setPositionAndUpdate(blockTarget.getX() + 0.5D, blockTarget.getY(), blockTarget.getZ() + 0.5D);
                attemptTimer = 0L; //Reset the attempt time after we've reached our target
            }

            attemptTimer++;
        }
    }
}
