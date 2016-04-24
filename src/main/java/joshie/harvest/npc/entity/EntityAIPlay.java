package joshie.harvest.npc.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

import java.util.Iterator;
import java.util.List;

public class EntityAIPlay extends EntityAIBase {
    private EntityNPC npc;
    private EntityLivingBase targetVillager;
    private double field_75261_c;
    private int playTime;
    private static final String __OBFID = "CL_00001605";

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
            List list = npc.worldObj.getEntitiesWithinAABB(EntityNPC.class, npc.getEntityBoundingBox().expand(6.0D, 3.0D, 6.0D));
            double d0 = Double.MAX_VALUE;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityNPC entityvillager = (EntityNPC) iterator.next();

                if (entityvillager != npc && !entityvillager.isPlaying() && entityvillager.getGrowingAge() < 0) {
                    double d1 = entityvillager.getDistanceSqToEntity(this.npc);

                    if (d1 <= d0) {
                        d0 = d1;
                        this.targetVillager = entityvillager;
                    }
                }
            }

            if (this.targetVillager == null) {
                Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.npc, 16, 3);

                if (vec3 == null) {
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
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.npc, 16, 3);

            if (vec3 == null) {
                return;
            }

            this.npc.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.field_75261_c);
        }
    }
}