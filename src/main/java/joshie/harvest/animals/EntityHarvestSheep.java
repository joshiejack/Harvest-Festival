package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityHarvestSheep extends EntitySheep implements IAnimalTracked {
    private AnimalData data;
    
    public EntityHarvestSheep(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        data = new AnimalData(this);
    }

    @Override
    public EntitySheep createChild(EntityAgeable ageable) {
        return new EntityHarvestSheep(this.worldObj);
    }
    
    @Override
    public IAnimalData getData() {
        return data;
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        data.readFromNBT(nbt);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        data.writeToNBT(nbt);
    }
}
