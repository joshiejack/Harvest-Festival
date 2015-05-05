package joshie.harvestmoon.animals;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.World;

public class EntityHarvestSheep extends EntitySheep {
    public EntityHarvestSheep(World world) {
        super(world);
        this.setSize(1.4F, 1.4F);
    }

    @Override
    public EntitySheep createChild(EntityAgeable ageable) {
        return new EntityHarvestSheep(this.worldObj);
    }
}
