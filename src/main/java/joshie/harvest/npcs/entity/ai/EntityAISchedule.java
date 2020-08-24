package joshie.harvest.npcs.entity.ai;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class EntityAISchedule extends EntityAIBase {
    private final EntityNPCHuman npc;
    private double distanceRequired;
    private int ticksBeforeTeleport;
    private BlockPos blockTarget;
    private BlockPos prevTarget;
    private int teleportTimer;
    private int scheduleTimer;

    public EntityAISchedule(EntityNPCHuman npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    @Nullable
    private BuildingLocation getBuildingTarget(CalendarDate date) {
        return npc.getNPC().getScheduler().getTarget(npc.world, npc, date.getSeason(), date.getWeekday(), CalendarHelper.getTime(npc.world));
    }

    @Override
    public boolean shouldExecute() {
        if (npc.getNPC() == null) return false;
        updateTarget(); //Called here as need to check for new targets
        return blockTarget == null || (npc.getDistanceSq(blockTarget) > distanceRequired);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return blockTarget != null && !npc.isTalking();
    }

    private void updateTarget() {
        CalendarDate date = HFApi.calendar.getDate(npc.world);
        BuildingLocation location = getBuildingTarget(date);
        if (location != null) {
            distanceRequired = location.distance;
            ticksBeforeTeleport = location.updatesBeforeTeleport;
            blockTarget = NPCHelper.getCoordinatesForLocation(npc, location);
            if (blockTarget != null) blockTarget = blockTarget.down();
        }
    }

    @Nullable
    private Path getPathToTarget() {
        Path path = npc.getNavigator().getPathToPos(blockTarget);
        if (path != null) return path;
        else {
            //Grab a random block towards the target
            Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, 16, 7, new Vec3d((double) blockTarget.getX() + 0.5D, (double) blockTarget.getY(), (double) blockTarget.getZ() + 0.5D));
            if (vec != null) {
                return npc.getNavigator().getPathToPos(new BlockPos(vec));
            } else return null;
        }
    }

    @Nullable
    private Path getPathAwayFromTarget() {
        Vec3d vec = RandomPositionGenerator.findRandomTargetBlockAwayFrom(npc, (int) distanceRequired / 2, 3, new Vec3d((double) blockTarget.getX() + 0.5D, (double) blockTarget.getY(), (double) blockTarget.getZ() + 0.5D));
        if (vec != null) {
            blockTarget = new BlockPos(vec); //Update the target, so we don't teleport back
            return npc.getNavigator().getPathToPos(blockTarget);
        } else return null;
    }

    private boolean shouldTeleport() {
        if (!blockTarget.equals(prevTarget)) teleportTimer = 0;
        else teleportTimer++; //Update the teleport timer if the target is the same
        prevTarget = blockTarget; //Update the prevTarget
        return teleportTimer >= ticksBeforeTeleport;
    }

    @Override
    public void updateTask() {
        if (scheduleTimer %200 == 0) updateTarget();
        if (blockTarget != null) {
            //If we're too far away from the target, based on the location requirements then
            if (npc.getDistanceSq(blockTarget) > distanceRequired) {
                if (scheduleTimer %100 == 0) {
                    if (shouldTeleport()) {
                        teleportTimer = 0; //Reset the teleport timer
                        npc.attemptTeleport(blockTarget.getX() + 0.5D, blockTarget.getY() + 1D, blockTarget.getZ() + 0.5D);
                    } else {
                        Path path = getPathToTarget();
                        if (path != null) {
                            npc.getNavigator().setPath(path, 0.6F);
                        }
                    }
                }
            } //else if (distanceRequired == 1) npc.getNavigator().clearPathEntity();
            else if (distanceRequired > 1 && scheduleTimer % 300 == 0) { //If the location is larger than 0, allow wandering
                Path path = getPathAwayFromTarget();
                if (path != null) {
                    npc.getNavigator().setPath(path, 0.6F);
                }
            }
        }

        scheduleTimer++;
    }
}
