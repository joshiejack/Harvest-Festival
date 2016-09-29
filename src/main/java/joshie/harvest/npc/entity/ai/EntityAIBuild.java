package joshie.harvest.npc.entity.ai;

import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIBuild extends EntityAIBase {
    private final EntityNPCBuilder npc;
    private int teleportTimer;
    private int buildingTimer;

    public EntityAIBuild(EntityNPCBuilder npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        return npc.getBuilding() != null;
    }

    @Override
    public boolean continueExecuting() {
        if (npc.getBuilding() == null) {
            teleportTimer = 0;
            return false;
        } else return true;
    }

    @Override
    public void updateTask() {
        BuildingStage building = npc.getBuilding();
        if (buildingTimer % building.getTickTime() == 0) {
            Placeable placeable = building.next();
            if (placeable != null) {
                BlockPos go = building.getPos(placeable);
                double distance = npc.getDistanceSq(go);
                boolean tooFar = distance >= building.getDistance(placeable);
                if (tooFar) {
                    //Teleportation
                    if (teleportTimer >= 100 || distance >= 256D) {
                        teleportTimer = 0;
                        npc.attemptTeleport(go.getX() + 0.5D, go.getY() + 1D, go.getZ() + 0.5D);
                        tooFar = false; //Force the placement of the block
                    }

                    teleportTimer++;

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
                    npc.getNavigator().setPath(npc.getNavigator().getPathToPos(go), 0.6F);
                    if (building.build(npc.worldObj)) npc.resetSpawnHome();
                    if (building.isFinished()) {
                        npc.finishBuilding();
                    }
                }

                //If we're suffocating
                if (!npc.isInsideOfMaterial(Material.AIR)) {
                    BlockPos pos = go.add(npc.worldObj.rand.nextInt(16) - 8, npc.worldObj.rand.nextInt(16), npc.worldObj.rand.nextInt(16) - 8);
                    if (EntityHelper.isSpawnable(npc.worldObj, pos) && EntityHelper.isSpawnable(npc.worldObj, pos.up())) {
                        npc.setPositionAndUpdate(go.getX() + 0.5D, go.getY() + 1D, go.getZ() + 0.5D);
                    }
                }
            }
        }

        buildingTimer++;
    }
}
