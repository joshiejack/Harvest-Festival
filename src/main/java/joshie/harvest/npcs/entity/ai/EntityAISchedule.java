package joshie.harvest.npcs.entity.ai;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPC.Location;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAISchedule extends EntityAIBase {
    private final EntityNPCHuman npc;
    private BuildingLocation location;
    private BlockPos blockTarget;
    private BlockPos prevTarget;
    private int teleportTimer;
    private int scheduleTimer;

    public EntityAISchedule(EntityNPCHuman npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    private BuildingLocation getBuildingTarget(CalendarDate date) { //If the npcs shop is open, they should try to go their job
        if (NPCHelper.isShopPreparingToOpen(npc, npc.worldObj)) return npc.getNPC().getLocation(Location.SHOP);
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
        return blockTarget != null;
    }

    private void updateTarget() {
        CalendarDate date = HFApi.calendar.getDate(npc.worldObj);
        location = getBuildingTarget(date);
        blockTarget = NPCHelper.getCoordinatesForLocation(npc, location);
    }

    @Override
    public void updateTask() {
        scheduleTimer++;
        if (scheduleTimer %200 == 0) updateTarget();
        if (blockTarget != null) {
            double distance = npc.getDistanceSq(blockTarget);
            boolean tooFar = distance > location.getDistanceRequired();
            if (tooFar) {
                if (scheduleTimer % 100 == 0) {
                    Path path = npc.getNavigator().getPathToPos(blockTarget);
                    if (path == null) {
                        Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, 32, 5, new Vec3d((double) blockTarget.getX() + 0.5D, (double) blockTarget.getY() + 1D, (double) blockTarget.getZ() + 0.5D));
                        if (vec != null) {
                            path = npc.getNavigator().getPathToPos(new BlockPos(vec));
                        }
                    }

                    npc.getNavigator().setPath(path, 0.6F);
                }

                if (!blockTarget.equals(prevTarget)) teleportTimer = 0;
                else teleportTimer++;
                prevTarget = blockTarget;
                if (teleportTimer >= 600) {
                    teleportTimer = 0;
                    npc.attemptTeleport(blockTarget.getX() + 0.5D, blockTarget.getY() + 1D, blockTarget.getZ() + 0.5D);
                }
            } else if (scheduleTimer %300 == 0) {
                Vec3d vec = RandomPositionGenerator.findRandomTargetBlockAwayFrom(npc, (int) location.getDistanceRequired() / 2, 3, new Vec3d((double) blockTarget.getX() + 0.5D, (double) blockTarget.getY() + 1D, (double) blockTarget.getZ() + 0.5D));
                if (vec != null) {
                    Path path = npc.getNavigator().getPathToPos(new BlockPos(vec));
                    if (path != null) {
                        npc.getNavigator().setPath(path, 0.6F);
                    }
                }
            }
        }
    }
}
