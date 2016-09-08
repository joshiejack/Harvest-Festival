package joshie.harvest.npc.entity.ai;

import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIBuild extends EntityAIBase {
    private EntityNPCBuilder npc;
    private int tick;

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
        return npc.getBuilding() != null;
    }

    @Override
    public void updateTask() {
        BuildingStage building = npc.getBuilding();
        if (tick % building.getTickTime() == 0) {
            if (building.build(npc.worldObj)) npc.resetSpawnHome();
            BlockPos go = building.next();
            npc.getNavigator().tryMoveToXYZ((double) go.getX() + 0.5D, (double) go.getY(), (double) go.getZ() + 0.5D, 0.75D);
            if (building.isFinished()) {
                npc.finishBuilding();
            }
        }

        tick++;
    }
}
