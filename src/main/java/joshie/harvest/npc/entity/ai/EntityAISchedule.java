package joshie.harvest.npc.entity.ai;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.npc.NPCHelper;
import joshie.harvest.npc.entity.EntityNPCHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static joshie.harvest.api.npc.INPC.Location.WORK;

public class EntityAISchedule extends EntityAIBase {
    private final EntityNPCHuman npc;
    private BuildingLocation location;
    private BlockPos blockTarget;
    private int teleportTimer;
    private int scheduleTimer;

    public EntityAISchedule(EntityNPCHuman npc) {
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
        updateTarget(); //Called here as need to check for new targets
        return blockTarget != null && npc.getDistanceSq(blockTarget) > location.getDistanceRequired();
    }

    @Override
    public boolean continueExecuting() {
        return blockTarget != null && npc.getDistanceSq(blockTarget) > location.getDistanceRequired();
    }

    private void updateTarget() {
        CalendarDate date = HFApi.calendar.getDate(npc.worldObj);
        location = getBuildingTarget(date);
        blockTarget = NPCHelper.getCoordinatesForLocation(npc, location);
    }

    @Override
    public void updateTask() {
        //Update the target
        if (scheduleTimer %200 == 0) updateTarget();
        if (blockTarget != null) {
            double distance = npc.getDistanceSq(blockTarget);
            boolean tooFar = distance > location.getDistanceRequired();
            if (scheduleTimer %15 == 0) {
                if (tooFar) {
                    //Teleportation
                    if ((teleportTimer >= 60 && distance <= 64D) || teleportTimer >= 300) {
                        teleportTimer = 0;
                        npc.attemptTeleport(blockTarget.getX() + 0.5D, blockTarget.getY() + 1D, blockTarget.getZ() + 0.5D);
                    }

                    teleportTimer++;

                    //Random coordinates
                    int move = (int) Math.min(32D, Math.max(1D, Math.ceil((distance / 4D))));
                    Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, move, 3, new Vec3d((double) blockTarget.getX() + 0.5D, (double) blockTarget.getY() + 1D, (double) blockTarget.getZ() + 0.5D));
                    if (vec != null) {
                        blockTarget = new BlockPos(vec);
                    }
                } else teleportTimer = 0;
            }

            //If the NPC is close the where they need to build
            if (tooFar) npc.getNavigator().tryMoveToXYZ(blockTarget.getX() + 0.5D, blockTarget.getY() + 1D, blockTarget.getZ() + 0.5D, 0.55D);
        }

        scheduleTimer++;
    }
}
