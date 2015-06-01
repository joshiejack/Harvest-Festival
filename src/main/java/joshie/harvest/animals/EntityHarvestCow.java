package joshie.harvest.animals;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.world.World;

public class EntityHarvestCow extends EntityCow {
    public EntityHarvestCow(World world) {
        super(world);
        this.setSize(1.4F, 1.4F);
    }

    @Override
    public EntityCow createChild(EntityAgeable p_90011_1_) {
        return new EntityHarvestCow(this.worldObj);
    }
}
