package joshie.harvest.player.relationships;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.UUID;

public class RelationshipHandlerEntity implements IRelatableDataHandler<IAnimalTracked> {
    @Override
    public String name() {
        return "entity";
    }

    @Override
    public void onMessage(World world, IAnimalTracked tracked, boolean particles) {
        EntityAnimal entity = tracked.getAsEntity();
        if (entity != null) {
            if (particles) {
                for (int j = 0; j < 3D; j++) {
                    double x = (entity.posX - 0.5D) + entity.worldObj.rand.nextFloat();
                    double y = (entity.posY - 0.5D) + entity.worldObj.rand.nextFloat();
                    double z = (entity.posZ - 0.5D) + entity.worldObj.rand.nextFloat();
                    world.spawnParticle(EnumParticleTypes.HEART, x, 1D + y - 0.125D, z, 0, 0, 0);
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
    public void writeToNBT(IAnimalTracked tracked, NBTTagCompound tag) {
        tag.setString("UUID", EntityHelper.getEntityUUID(tracked.getAsEntity()).toString());
        tag.setInteger("Dimension", tracked.getAsEntity().worldObj.provider.getDimension());
    }
}
