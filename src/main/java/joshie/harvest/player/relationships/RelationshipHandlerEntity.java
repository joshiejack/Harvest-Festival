package joshie.harvest.player.relationships;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;

import java.util.UUID;

public class RelationshipHandlerEntity implements IRelatableDataHandler<IAnimalTracked> {
    @Override
    public String name() {
        return "entity";
    }

    @Override
    public void onMessage(IAnimalTracked tracked, boolean particles) {
        EntityAnimal entity = tracked.getData().getAnimal();
        if (entity != null) {
            if (particles) {
                for (int j = 0; j < 103D; j++) {
                    double d8 = (entity.posX - 0.5D) + entity.worldObj.rand.nextFloat();
                    double d7 = (entity.posY - 0.5D) + entity.worldObj.rand.nextFloat();
                    double d9 = (entity.posZ - 0.5D) + entity.worldObj.rand.nextFloat();
                    entity.worldObj.spawnParticle(EnumParticleTypes.HEART, d8, 1.0D + d7 - 0.125D + 2D, d9, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public IAnimalTracked readFromNBT(NBTTagCompound tag) {
        UUID uuid = UUID.fromString(tag.getString("UUID"));
        int dimension = tag.getInteger("Dimension");
        return (IAnimalTracked) EntityHelper.getAnimalFromUUID(dimension, uuid);
    }

    @Override
    public void writeToNBT(IAnimalTracked relatable, NBTTagCompound tag) {
        tag.setString("UUID", UUIDHelper.getEntityUUID(relatable.getData().getAnimal()).toString());
        tag.setInteger("Dimension", relatable.getData().getAnimal().worldObj.provider.getDimension());
    }
}
