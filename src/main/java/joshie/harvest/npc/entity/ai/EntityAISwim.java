package joshie.harvest.npc.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAISwim extends EntityAIBase {
    private final EntityLiving theEntity;

    public EntityAISwim(EntityLiving entitylivingIn) {
        theEntity = entitylivingIn;
        setMutexBits(4);
        ((PathNavigateGround) entitylivingIn.getNavigator()).setCanSwim(true);
    }

    @Override
    public boolean shouldExecute() {
        return theEntity.isInWater() || theEntity.isInLava();
    }

    @Override
    public void updateTask() {
        if (theEntity.isInsideOfMaterial(Material.WATER)) {
            theEntity.motionY = 0.2F;
        } else theEntity.motionY = 0.1F;
    }
}