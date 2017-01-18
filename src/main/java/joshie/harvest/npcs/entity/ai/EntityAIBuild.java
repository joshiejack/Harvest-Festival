package joshie.harvest.npcs.entity.ai;

import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.npcs.entity.EntityNPCBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIBuild extends EntityAIBase {
    private final EntityNPCBuilder npc;
    private int teleportTimer;
    private int buildingTimer;
    private int stuckTimer;
    private BlockPos prev;

    public EntityAIBuild(EntityNPCBuilder npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (npc.getBuilding() != null) {
            npc.stepHeight = 1F;
            return true;
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        if (npc.getBuilding() == null) {
            teleportTimer = 0;
            return false;
        } else return true;
    }

    private void attemptToTeleportToSafety(BlockPos go) {
        Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, 3, 32, new Vec3d((double) go.getX() + 0.5D, (double) go.getY() + 1D, (double) go.getZ() + 0.5D));
        if (vec != null) {
            BlockPos pos = new BlockPos(vec);
            if (EntityHelper.isSpawnable(npc.worldObj, pos) && EntityHelper.isSpawnable(npc.worldObj, pos.up()) && npc.worldObj.getBlockState(pos.down()).isSideSolid(npc.worldObj, pos.down(), EnumFacing.UP)) {
                npc.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
            }
        }
    }

    @Override
    public void updateTask() {
        BuildingStage building = npc.getBuilding();
        if (buildingTimer % building.getTickTime() == 0) {
            Placeable placeable = building.next();
            if (placeable != null) {
                BlockPos go = building.getPos(building.previous());
                double distance = npc.getDistanceSq(go);
                boolean tooFar = distance >= building.getDistance(placeable);
                if (tooFar) {
                    //Teleportation
                    if (teleportTimer >= 200 || distance >= 4096D) {
                        teleportTimer = 0;
                        npc.attemptTeleport(go.getX() + 0.5D, go.getY() + 1D, go.getZ() + 0.5D);
                        tooFar = false; //Force the placement of the block
                    }

                    BlockPos current = new BlockPos(npc);
                    teleportTimer += current.equals(prev) ? 10: 1;
                    prev = current;

                    //Update the path
                    Path path = npc.getNavigator().getPathToPos(go);
                    if (path == null) {
                        Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, 32, 5, new Vec3d((double) go.getX() + 0.5D, (double) go.getY() + 1D, (double) go.getZ() + 0.5D));
                        if (vec != null) {
                            path = npc.getNavigator().getPathToPos(new BlockPos(vec));
                        }
                    }

                    npc.getNavigator().setPath(path, 0.6F);
                } else teleportTimer = 0;

                //If the NPC is close the where they need to build
                if (!tooFar) {
                    if (building.build(npc.worldObj)) {
                        npc.getNavigator().setPath(npc.getNavigator().getPathToPos(go), 0.85D);
                        stuckTimer = 0;
                    } else {
                        Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, 1, 1, new Vec3d((double) go.getX() + 0.5D, (double) go.getY() + 1D, (double) go.getZ() + 0.5D));
                        if (vec != null) {
                            npc.getNavigator().setPath(npc.getNavigator().getPathToPos(new BlockPos(vec)), 0.85F);
                        }


                        stuckTimer++;
                        if (stuckTimer >= 100) {
                            stuckTimer = 0;
                            building.build(npc.worldObj);
                            attemptToTeleportToSafety(go);
                        }
                    }

                    //Finish the building
                    if (building.isFinished()) {
                        npc.finishBuilding();
                        npc.resetSpawnHome();
                    }
                }

                //If we're suffocating
                BlockPos pos = new BlockPos(npc);
                if (!npc.isInsideOfMaterial(Material.AIR)) {
                    attemptToTeleportToSafety(go);
                } else if (npc.worldObj.getBlockState(pos).getBlock().isPassable(npc.worldObj, pos)) {
                    npc.setJumping(true);
                }
            }
        }

        buildingTimer++;
    }
}
