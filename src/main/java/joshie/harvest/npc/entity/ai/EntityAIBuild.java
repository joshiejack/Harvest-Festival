package joshie.harvest.npc.entity.ai;

import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
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
                    if ((teleportTimer >= 60 && distance <= 64D) || teleportTimer >= 200) {
                        teleportTimer = 0;
                        npc.attemptTeleport(go.getX() + 0.5D, go.getY() + 1D, go.getZ() + 0.5D);
                        tooFar = false; //Force the placement of the block
                    }

                    teleportTimer++;

                    //Random coordinates
                    int move = distance >= 100D ? 10 : 2;
                    Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(npc, move, 3, new Vec3d((double) go.getX() + 0.5D, (double) go.getY() + 1D, (double) go.getZ() + 0.5D));
                    if (vec != null) {
                        go = new BlockPos(vec);
                    }
                } else teleportTimer = 0;

                //If the NPC is close the where they need to build
                if (!tooFar) {
                    npc.getNavigator().tryMoveToXYZ(go.getX() + 0.5D, go.getY() + 1D, go.getZ() + 0.5D, 0.65D);
                    if (building.build(npc.worldObj)) npc.resetSpawnHome();
                    if (building.isFinished()) {
                        npc.finishBuilding();
                    }
                } else npc.getNavigator().tryMoveToXYZ(go.getX() + 0.5D, go.getY() + 1D, go.getZ() + 0.5D, 0.85D);

                //If we're suffocating
                if (npc.isEntityInsideOpaqueBlock()) {
                    BlockPos pos = go.add(npc.worldObj.rand.nextInt(8) - 4, npc.worldObj.rand.nextInt(3), npc.worldObj.rand.nextInt(8) - 4);
                    if (MiningHelper.isSpawnable(npc.worldObj, pos)) {
                        npc.setPositionAndUpdate(go.getX() + 0.5D, go.getY() + 1D, go.getZ() + 0.5D);
                    }
                }
            }
        }

        buildingTimer++;
    }
}
