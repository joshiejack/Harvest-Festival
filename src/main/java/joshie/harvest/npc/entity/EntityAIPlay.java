package joshie.harvest.npc.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;
import java.util.List;

public class EntityAIPlay extends EntityAIBase {
    private EntityNPC npc;
    private EntityLivingBase targetVillager;
    private double field_75261_c;
    private int playTime;

    public EntityAIPlay(EntityNPC npc, double p_i1646_2_) {
        this.npc = npc;
        this.field_75261_c = p_i1646_2_;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (npc.getGrowingAge() >= 0) {
            return false;
        } else if (npc.getRNG().nextInt(400) != 0) {
            return false;
        } else {
            List<EntityNPC> list = npc.worldObj.getEntitiesWithinAABB(EntityNPC.class, npc.getEntityBoundingBox().expand(6.0D, 3.0D, 6.0D));
            double d0 = Double.MAX_VALUE;
            Iterator<EntityNPC> iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityNPC entityvillager = iterator.next();

                if (entityvillager != npc && !entityvillager.isPlaying() && entityvillager.getGrowingAge() < 0) {
                    double d1 = entityvillager.getDistanceSqToEntity(this.npc);

                    if (d1 <= d0) {
                        d0 = d1;
                        this.targetVillager = entityvillager;
                    }
                }
            }

            if (this.targetVillager == null) {
                Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.npc, 16, 3);

                if (vec3d == null) {
                    return false;
                }
            }

            return true;
        }
    }

    @Override
    public boolean continueExecuting() {
        return this.playTime > 0;
    }

    @Override
    public void startExecuting() {
        if (this.targetVillager != null) {
            this.npc.setPlaying(true);
        }

        this.playTime = 1000;
    }

    @Override
    public void resetTask() {
        this.npc.setPlaying(false);
        this.targetVillager = null;
    }

    @Override
    public void updateTask() {
        --this.playTime;

        if (this.targetVillager != null) {
            if (this.npc.getDistanceSqToEntity(this.targetVillager) > 4.0D) {
                this.npc.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.field_75261_c);
            }
        } else if (this.npc.getNavigator().noPath()) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.npc, 16, 3);

            if (vec3d == null) {
                return;
            }

            this.npc.getNavigator().tryMoveToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, this.field_75261_c);
        }
    }
}