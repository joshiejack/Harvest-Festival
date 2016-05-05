package joshie.harvest.npc.entity;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIGoToBlock extends EntityAIBase {
    private EntityNPCBuilder entity;
    private BlockPos insidePos = new BlockPos(-1, -1, -1);

    public EntityAIGoToBlock(EntityNPCBuilder npc) {
        entity = npc;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        return entity.headTowards != null;
    }

    @Override
    public boolean continueExecuting() {
        return entity.headTowards != null && !this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        if (this.entity.getDistanceSq(entity.headTowards) > 256.0D) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(entity, 14, 3, new Vec3d((double) entity.headTowards.getX() + 0.5D, (double) entity.headTowards.getY(), (double) entity.headTowards.getZ() + 0.5D));
            if (vec3d != null) {
                this.entity.getNavigator().tryMoveToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
            }
        } else {
            this.entity.getNavigator().tryMoveToXYZ((double) entity.headTowards.getX() + 0.5D, (double) entity.headTowards.getY(), (double) entity.headTowards.getZ() + 0.5D, 1.0D);
        }
    }

    @Override
    public void resetTask() {
        this.insidePos = entity.headTowards;
    }
}